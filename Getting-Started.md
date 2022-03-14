# Getting Started

## Booting the server
Start the server with default options:

`Ziva.boot;`

## Loading samples
Load some samples by providing a path to the parent directory containing subdirectories with audio files.
As an example we'll be using the great SuperDirt samples set. The "SuperDirt" quark can be installed (see the [README](./README.md)) and then load its samples with:

`Ziva.loadSamples(Platform.userAppSupportDir ++ "/downloaded-quarks/Dirt-Samples");`

This should list all the sample names (with the number files per name between `()`) in the post window. If it doesn't, or you want to invoke it again later, use

`Ziva.sounds;`

New samples can be added with new calls to (repeating names will be replaced):

`Ziva.loadSamples("/path/to/your/samples/parent/dir");`

## Basic functions

Assing a sound to a variable:

`~lola = Pbind(\type, \sample, \sound, \birds);`

Play the sound:

`[ ~lola ].ziva.play;`

Stop the sound:

```
[ nil ].ziva.play;
// or 
Ziva.stop;
```

Play the sound fast:

`[ ~lola.fast ].ziva.play;`

Play it faster:

`[ ~lola.faster ].ziva.play;`

Play it as fast as you can:

`[ ~lola.fastest ].ziva.play;`

Play it slow:

`[ ~lola.slow ].ziva.play;`

Try playing it slower and as slow as you can :)

Fro custom durations:

`[ ~lola.dur(0.628) ].ziva.play;`
 
----

Play it loud (mezzo forte):

`[ ~lola.f ].ziva.play;`

Play it louder (forte):

`[ ~lola.ff ].ziva.play;`

Play it even louder (fortíssimo):

`[ ~lola.fff ].ziva.play;`

Play it quiter (mezzo piano);

`[ ~lola.p ].ziva.play;`

you get the drill.
