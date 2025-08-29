# Hyflip Mod
A mod designed to bring flips right to your Minecraft client. Uses the powerful hyflip backend server, and can be paired
with the website for extra efficiency.

Note: I don't know Kotlin that well </3

# TODO:
- [x] Work on a config UI first. The getting-data-from-backend stuff shouldn't be too hard, but idk how any Minecraft-specific
java/kotlin code works so that is a priority.
- [] Use the user config received from the backend into config.json. Both are in JSON but ensure their format is similar OR add a translation layer.
- [] If the user makes a change in the default config and then closes the menu, send it over to the server. Before that however, request the config to ensure
that the config WITHOUT the change isn't different, and if it is ask the user "Do you want to keep your changes or pull from the server?"