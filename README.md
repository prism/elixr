# elixr

elixr is a utility API for Bukkit-based plugins. All classes contain static methods that you can use from your plugin - no other dependancies other than elixr iteself. Elixr is being released as open source with the hope that you'll use and contribute new code!

elixr was originally extracted from code too-often shared between our own plugins including Prism, Craftys, DarkMythos, Inventory Toolkit, Darmok, dhmcDeath, and more.

#### Basic Utility Overview

- Blocks - Including finding block relationships, handling double-length blocks, and more.
- Chunk - Identifying the edges of chunks, min/max vectors.
- Date - Converting to/from in-game date strings like Prism's time parameter
- Death - Propertly identifying the killer/victim, weapon, and more
- Enchantment - Name conversion utilities
- Entity - Location, nearby items, etc.
- Experience - Properly add and substract XP
- Inventory - Logic for items in an inventory, stacking, sorting, moving, and more
- Item - Logic for full item names, percentage used, quantity strings, and more
- Type - Number/string logic


#### Get Help

IRC: irc.esper.net, room: #prism

[Docs](http://refract.dhmc.us/elixr/docs/)

[Bug Tracking](https://snowy-evening.com/botsko/elixr/)


#### Maven

You may include the package with your plugin via maven, our repo is at:

`http://dhmc.us:8081/nexus/content/repositories/`


    <dependency>
	    <groupId>me.botsko</groupId>
	    <artifactId>elixr</artifactId>
	    <version>1.0-dev-27-gba985bf-SNAPSHOT</version>
	</dependency>
