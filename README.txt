ndig is a haven-type chat server mixed with a MUD.

Future plans:
* embedded neo4j may not be the way to go. With a server I get http management
  and the ability to use a better API.
* furnishings
* fights!

* Registration

Proposed augmentation to the ".name" command to support registration of names, i.e., association of
passwords.

    Add logged_in flag to NdPlayer.

    Update .name command to reject '=' symbol in names. All name matching must be case-insensitive.

    Upon receipt of ".n <newname>" command (without password):
    If player's name matches <newname>, issue error message.
    Next check to see if <newname> is already in use; if it is, respond with error message.
    Next check to see if <newname> is in the name registry. If it is, respond with message:
      The name "newname" is registered. If it belongs to you, try ".n newname=[password]".
    If it is not in the registry, and not in use, update the player's name. Include text
      'this name is unregistered' in the response.

    Upon receipt of ".n <newname>=<password>" command (with password):
    If player is logged in with name <newname>:
      If <password> is not empty, update the password in the registry.
      If <password> is empty, un-register <newname> and set logged_in to false.
    Otherwise (player not logged in, or logged in under another name):
      If <newname> found in registry with password, update player's name and set
        logged_in to true.
      If <newname> found in registry with other password, auth failure message.
      If <newname> is currently in use, issue error message.
      Otherwise (<newname> not in registry and not in use):
        Add <newname,password> to registry, update the player's name, and set
          logged_in to true.

Some other rules about registered names:

    "Zubin" and any name beginning with "sysop" are reserved. Names must be at least
    3 characters, must be made up of [a-zA-Z0-9 \.,:;'"\-_], may not include more than
    one consecutive space, and must start with a letter or number.


.n Zubin=rascals
> Incorrect password.

.n nobody
> Name changed; this name is unregistered.

.n nobody=lovesme
> You have registered the name "nobody". Don't forget your password, you'll need it to use this name again.

.n nobody=
> Password removed; the name "nobody" is no longer registered.

