# Seamus' Arsenal

This a plugin I made in order to add guns to your minecraft server.
This is not intended for survival use, but rather for minigames and such.

The only guns configured by default are the pistol and sniper, you can add as many as you want though!

#how the guns work
right clicking does a raycast doing the damage specified in the config to any entity hit.
the item type is put on a short cooldown according to the fire rate specified in the config
the durability goes down, the durability acts as a percentage of the mag left

if you are out of ammo the cooldown is set really high until the reload time specified in the config is over
the durability is then reset along with the cooldown and the weapon is ready to be used.

you can give your players and item that uses durability and has the weapons name as the display name to use the gun
you can also use the built in command /giveweapon <player> <item type> <gun name> to give them the named tool
this command may be run from console, player, or block

# Gun Stats
required traits:
fireRate (seconds, double)
After firing once via right clicking, this is how many seconds until you are permitted to fire again

damage (1/2 hearts, double)
When a bullet hits an entity this is how much damage the entity will suffer.

clipSize (int)
This is how many shots a weapon can fire before requiring reload (automatically happens when firing with no ammo)

reloadTime (seconds,double)
This is how long reloads take in seconds.

range (blocks,double)
This is how long the raycast will be, thus any entities outside this range are not ever counted as hit.

bulletSize (blocks,double)
This tells the game how large the raycast should be, useful for deciding how precise the bullet hitbox should be.

non-required traits:
particle (Enum from org.bukkit.Particle)
This is the particle that is displayed on the impact point if hitting a block (must be from org.bukkit.Particle)

#Planned features
Weapon types such as:
* projectile
* shotgun
* grenade
* explosive

Particle trails for the bullets

A better way to distinguish from a gun and a regular tool (currently its just the display name)

Sounds (customizable through config like other stats)

#non-planned features
I am very unlikely to add the following:
* chests containing random weapons (seems like this could be a different plugin)
* reserve ammo (limit magazines)
* json support (I've tried this, its a pain)
* A weapon type that mimmicks X weapon from Y game, this is open source, if you can read my bad code you can add that niche thing yourself.
* holming weapons (out of my personal skill level)