when user connects assign lowest available line number from 1 to LIMIT
player has connected_at and last_seen_at for "on" and "idle" times
Admin gets #0

COMMANDS
:emote to room
.p# private message to line #
.y yell to server
.y :emote to server
.h hush yells
.i# ignore all messages from #
.n name[=password]
.w# who is on line #
.w who is on server
.q quit
.? help
anything still unmatched speaks to room

Displays as:

(1, Zubin) hello
(*1, Zubin*) HELLO!
(1p, Zubin) psst hey

NdCommand(Player player, Game game, String msg)
  NdHushCommand
    player.setFlag('hush')
  NdWhoCommand
    player.tell game.getAllPlayers().map(:info)
  NdQuitCommand
    player.shutdown
  NdHelpCommand
    player.tell game.getAllCommands().map(:help)
  NdEmoteCommand
    player.getLocation().getAllPlayers().ignore(player).map(tell emote(msg))
  NdPrivateMessageCommand
    recipient = game.getPlayerByLine(line_number)
    if recipient.ignoring(player)
      player.tell "That user is ignoring you"
    else
      recipient.tell private(msg)
  NdYellCommand
    game.getAllPlayers.ignore(player).map(tell yell(msg))
  NdIgnoreCommand
    player.setIgnore(line_number)
  NdNameCommand
    player.setName(msg)
  NdWhoisCommand
    recipient = game.getPlayerByLine(line_number)
    player.tell recipient.info
  NdSayCommand
    player.getLocation().getAllPlayers().ignore(player).map(tell msg)