<meta name="description" content="Programming multi-agent in Java : use of an updated version of the Jade 
platform. Materials for Jade Tutorial : communication, protocols, votes, services, behaviors, ..." />

# Programming agents in Jade
## Tutorials

Set of exercises on multi-agent programming with the JADE platform.

To run these codes, it is necessary to import the library "[JadeUPHF.jar](https://github.com/EmmanuelADAM/JadeUPHF/blob/master/JadeUPHF.jar)".
This library is an update, with JAVA 17, of the last official version of  [Jade]
(https://jade.tilab.com) of Tilab and add functionalities to easier the implementation of agents.

The source of this new version, the library and notes, can be accessed from here : [JadeUPHF](https://emmanueladam.github.io/JadeUPHF/)".

_ Java >= 17 is required._


----


### Agents without behavior or communication

- Enchères : [englishAuctionOpenCry](https://github.com/EmmanuelADAM/jade/tree/english/td/englishAuctionOpenCry)
- Négociation : [negociationWindow](https://github.com/EmmanuelADAM/jade/tree/english/td/negociationWindow)
- Agencement : [timeTable](https://github.com/EmmanuelADAM/jade/tree/english/td/timeTable)
- TicTacMoe : TicTacToe à 3. 1 personne contre 1 personne contre 1 agent. Ou 1 personne contre 2 agents.
  - Reprenez le code du [OXO](https://github.com/EmmanuelADAM/IntelligenceArtificielleJava/tree/master/MCTS/OXO) pour l'inclure dans un agent. 
  - L'interface est purement textuelle pour simplifier.
- Solutions utilitaires, de bien être social : reprenez le code d'un vote simple (comme [BordaCount](https://github.com/EmmanuelADAM/jade/tree/english/protocols/bordaCount)). Cette fois l'initiateur (la place de marché) demande l'utilité des restos à chaque agent (une note) et opte pour la solution la plus utile, ou la solution de bien être social. 