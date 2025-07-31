# TP JADE - Gestion d'un Emploi Du Temps - Liam BOUDADI

## Fonctionnement de l’application

L’application gère la planification (EDT) via des agents JADE :
### Agents Professeurs (TeacherAgent)
Chaque enseignant (e1, e2, e3) doit proposer deux cours de deux heures sur deux jours.
-  Contraintes horaires : 
Chaque enseignant a deux créneaux à éviter (avec un ordre d’importance, p1 > p2)
-  Comportement :
  1. Au démarrage, le TeacherAgent initialise ses contraintes et envoie une première demande (via un OneShotBehaviour) à l’agent secrétaire pour obtenir la liste des créneaux disponibles.
  2. Dans son CyclicBehaviour, il reçoit les propositions (message INFORM) et calcule un score de satisfaction.
  3. Si la satisfaction est suffisante (≥ 0,8) et que le nombre de créneaux assignés n’est pas encore atteint, il sélectionne les créneaux les mieux adaptés (en filtrant ceux ne conflit pas avec ses contraintes) et les notifie à l’agent secrétaire.
  4. En cas de résultat insuffisant et tant que le nombre maximal de tentatives n’est pas atteint, il renvoie une nouvelle demande.
### Agent Secrétaire (SecretaryAgent)
L’agent secrétaire gère l’ensemble des salles et des créneaux horaires.
- Salles et créneaux :
Les salles (s1, s2, s3) possèdent des contraintes (créneaux indisponibles) et deux d’entre elles possèdent un rétroprojecteur.
Les créneaux possibles sont générés (par exemple : jours 1 et 2, heures 8, 10, 14, 16).
- Comportement :
  1. Il attend les demandes (REQUEST) et les notifications de sélection de créneaux (INFORM).
  2. Lorsqu’une demande est reçue, il parcourt l’ensemble des créneaux et, pour chaque salle disponible pour le créneau (vérification via la méthode isAvailable()), il crée une instance de TimeSlot associée à la salle et au professeur demandeur.
  3. Il renvoie ensuite la liste des créneaux disponibles au professeur.
  4. Lorsqu’un créneau sélectionné est reçu, il le « booke » dans la salle concernée et met à jour un compteur d’affectations par professeur.
  5. Quand tous les professeurs ont au moins deux créneaux assignés, l’agent affiche l’emploi du temps des salles.

## Points de vue multi-agent
### Mécanisme de résolution :
Le système repose sur une négociation par requêtes et réponses (vote/filtrage) dans lequel chaque enseignant évalue les propositions du secrétaire et renvoie sa sélection. Le protocole est simple et itératif (possibilité de renvoyer une nouvelle demande si la satisfaction n’est pas suffisante).

### Rôles et comportements :

- TeacherAgent utilise :
    - OneShotBehaviour pour initier la demande.
    - CyclicBehaviour pour écouter les réponses et traiter les créneaux proposés.
- SecretaryAgent utilise un CyclicBehaviour pour écouter en permanence les messages de type REQUEST et INFORM, pour proposer des créneaux (en tenant compte des contraintes des salles) et réserver les créneaux choisis. 
### Composition de l'environnement :
L’environnement comprend trois agents professeurs et un agent secrétaire, avec un ensemble de salles et de créneaux horaires qui représentent l’espace des possibilités pour la planification.

## Diagrammes

### Diagramme de classes
```plantuml
@startuml
' Classe représentant une salle
class Room {
  - String name
  - boolean hasProjector
  - List<TimeSlot> unavailableSlots
  - List<TimeSlot> bookedSlots
  + Room(String name, boolean hasProjector)
  + boolean isAvailable(TimeSlot slot)
  + void bookSlot(TimeSlot slot)
}

' Classe représentant un créneau horaire
class TimeSlot {
  - int day
  - int startHour
  - int endHour
  - Room room
  - String teacher
  - String group
  + TimeSlot(int day, int startHour)
  + int getDay()
  + int getStartHour()
  + int getEndHour()
  + Room getRoom()
  + String getTeacher()
  + String getGroup()
  + void setRoom(Room room)
  + void setTeacher(String teacher)
  + void setGroup(String group)
  + boolean equals(Object o)
  + int hashCode()
}

' Agent professeur
class TeacherAgent {
  - String teacherName
  - List<TimeSlot> constraints
  - List<TimeSlot> assignedSlots
  - final double p1 = 0.6
  - final double p2 = 0.4
  - int attemptCounter
  - final int maxAttempts = 1
  + void setup()
  - void initializeConstraints()
  - void requestSlots()
  - double calculateSatisfaction(List<TimeSlot> slots)
  - boolean conflictsWithConstraint(TimeSlot slot, int constraintIndex)
  - void selectBestSlots(List<TimeSlot> proposedSlots)
  - void informSecretary(TimeSlot slot)
  - void printSchedule()
}

' Agent secrétaire
class SecretaryAgent {
  - List<Room> rooms
  - List<TimeSlot> allSlots
  - Map<String, Integer> teacherAssignments
  + void setup()
  - void initializeAllSlots()
  - void initializeRoomConstraints()
  - List<TimeSlot> findAvailableSlots(String teacher)
  - void bookSlot(TimeSlot slot)
  - void handleSlotSelection(ACLMessage msg)
  - void printRoomSchedules()
}

' Classe principale pour lancer le système
class SchedulingSystem {
  + static void main(String[] args)
}

Room "1" *-- "many" TimeSlot : utilise
TeacherAgent ..> TimeSlot
SecretaryAgent ..> Room
SecretaryAgent ..> TimeSlot
SchedulingSystem --> TeacherAgent
SchedulingSystem --> SecretaryAgent
@enduml
```

### Diagramme d'états (pour TeacherAgent)
```plantuml
@startuml
[*] --> Idle

Idle --> RequestingSlots : Démarrage\n(one-shot behaviour)
RequestingSlots --> WaitingForResponse : Envoi de la demande\n(request_slots)
WaitingForResponse --> EvaluatingSlots : Réception du message INFORM\n(propositions de créneaux)
EvaluatingSlots --> SelectingSlots : Satisfaction ≥ seuil (>= 0.8)
EvaluatingSlots --> RequestingSlots : Créneaux insuffisants\net tentative possible
SelectingSlots --> WaitingForResponse : Envoi du créneau sélectionné\n(INFORM à secrétaire)
WaitingForResponse --> Finalized : Nombre de créneaux suffisant
Finalized --> [*]
@enduml
```

### Diagramme d'acitivité (flux de planification)
```plantuml
@startuml
start
:TeacherAgent envoie REQUEST\n("request_slots") à SecretaryAgent;
:SecretaryAgent reçoit REQUEST;
:SecretaryAgent exécute findAvailableSlots(teacher);
:SecretaryAgent envoie INFORM avec la liste\ndes créneaux disponibles;
:TeacherAgent reçoit le message INFORM;
:TeacherAgent calcule la satisfaction\ndes créneaux reçus;
if (Satisfaction >= seuil?) then (oui)
  :TeacherAgent sélectionne les créneaux compatibles;
  :TeacherAgent envoie INFORM avec le créneau sélectionné;
else (non)
  :Si nombre de tentatives < max\nalors renvoyer REQUEST;
endif
:SecretaryAgent reçoit le créneau sélectionné;
:SecretaryAgent booke le créneau\n(via bookSlot);
:Vérification de l'état global\n(des affectations enseignants);
if (Tous les enseignants ont 2 créneaux?) then (oui)
  :SecretaryAgent affiche l'emploi du temps des salles;
endif
stop
@enduml
```

### Diagramme de séquence
```plantuml
@startuml
actor TeacherAgent
actor SecretaryAgent

== Premier échange : demande de créneaux ==
TeacherAgent -> SecretaryAgent: REQUEST ("request_slots")
activate SecretaryAgent
SecretaryAgent -> SecretaryAgent: findAvailableSlots(teacher)
SecretaryAgent --> TeacherAgent: INFORM (liste de TimeSlot)
deactivate SecretaryAgent

== Traitement par l'agent professeur ==
TeacherAgent -> TeacherAgent: calculateSatisfaction(proposedSlots)
alt Satisfaction suffisante
   TeacherAgent -> TeacherAgent: selectBestSlots(proposedSlots)
   TeacherAgent -> SecretaryAgent: INFORM (créneau sélectionné)
else Tentative supplémentaire
   TeacherAgent -> SecretaryAgent: REQUEST ("request_slots")
end

== Réception et réservation ==
activate SecretaryAgent
SecretaryAgent -> SecretaryAgent: handleSlotSelection(msg)
SecretaryAgent -> SecretaryAgent: bookSlot(selected TimeSlot)
deactivate SecretaryAgent
@enduml
```

### Diagramme de dialogue JADE entre les agents
```plantuml
@startuml
participant "TeacherAgent\n(OneShotBehaviour / CyclicBehaviour)" as TA
participant "SecretaryAgent\n(CyclicBehaviour)" as SA

TA -> SA: REQUEST("request_slots")
note right: OneShotBehaviour : demande initiale
activate SA
SA -> SA: Exécute findAvailableSlots(teacher)
SA --> TA: INFORM(JSON liste de TimeSlot)
deactivate SA

TA -> TA: calculateSatisfaction()
note right: CyclicBehaviour : évaluation des créneaux reçus
alt Satisfaction ≥ 0.8 et créneaux insuffisants < 6
    TA -> TA: selectBestSlots(proposedSlots)
    TA -> SA: INFORM(JSON TimeSlot sélectionné)
    note right: CyclicBehaviour : envoi de la sélection
else
    TA -> SA: REQUEST("request_slots")
    note right: Nouvelle demande (tentative supplémentaire)
end

activate SA
SA -> SA: handleSlotSelection(msg)
SA -> SA: bookSlot(TimeSlot)
note right: Mise à jour des affectations et réservation en salle
deactivate SA

note over SA: Si tous les enseignants ont au moins 2 créneaux,\nSecretaryAgent affiche l'emploi du temps des salles.
@enduml
```

