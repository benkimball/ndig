ndig is a haven-type chat server mixed with a MUD.

TODO: when a player moves to a room, they're moved to a different instance of that
  room than everyone else who has moved there.

TODO: embedded neo4j may not be the way to go. With a server I get http management
  and the ability to use a better API.

TODO: methods should raise exceptions rather than returning null for errors

TODO: better ambiguity resolution, e.g., >n expands to >north if north direction exists

gdb holds:
  node "Home"
    -EXIT("south")-> node "Aussie"
    -EXIT("north")-> node "Boston"
  node "Aussie"
    -EXIT("north")-> node "Home"
  node "Boston"
    -EXIT("south")-> node "Home"


server startup
  merges node Home, instantiates and stores associated NdRoom
  loads names of exits of node Home, stores them in NdRoom

player login
  assigns player to home NdRoom

player moves
  if target NdRoom instance does not exist, read node from gdb and create it
  reassign player to new instance

player digs
  create new node in gdb
  create bidirectional relationships between source and target node
  instantiate new NdRoom from new node/rels
  persist node/rels in gdb
  reassign player to new instance

