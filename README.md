# Exchange Mage

Welcome to *Exchange Mage*, a deck builder/dungeon crawler indie game inspired by titles like *Slay the Spire*, *Darkest Dungeon*, and board games such as *Aeon's End*. The project aims to provide an experience familiar to fans of classic deck builder games with a few interesting twists and extra heavy emphasis on deck synergies.

## 🎮 Overview

In *Exchange Mage* the player will take on the role of a student of magical arts burdened with tuition debt who, while on a university exchange, is offered the possibility of earning back his financial independence. They can do so by retrieving valuable artifacts from a derelict campus library where an unspecified "academic incident" took place a few years back.

During your escapades into procedurally generated "dungeons" you will face foes, retrieve strange antiquities and gather pages from magical treaties to build a perfect deck of spells, hexes and enchantments.

## 🛠️ Development State

The project is currently in its early stages of development. As of now the component being worked on is the `logic layer` of the game, which is decoupled from the `visual layer` (the work on which will begin when all crucial parts of the `logic layer` are finished and fully tested). The `visual layer` will be implemented with the help of the open-source game-development framework - [libGDX](https://github.com/libgdx/libgdx).

## 🧩 Extensibility and Custom Content

*Exchange Mage* is developed with future extensibility and openness to custom content creation in mind. The design of the game's logic  takes full advantage of OOP principles and employment of design patterns to allow for easy creation of complex cards, effects, and enemy encounters while maintaining extensibility and modularity. It is my intent for anyone willing to familiarize themselves with the very basics of the game's logic to be able to easily create their own content for it, should they wish to do so.

To this end all parts of the game’s code are thoroughly documented with the logic behind how given mechanics work being explained where appropriate. I also try to regularly update the [UML class diagram](https://github.com/k-kalita/exchange-mage/blob/dev_logic_layer/docs/class_diagram.svg) of the `logic layer`, which in the future will be used to create a comprehensive guide to the game's rules and mechanics, as well as a brief handbook on how to create your own custom content for the game.

## License

*Exchange Mage* is licensed under the [GPL-3.0 license](LICENSE).