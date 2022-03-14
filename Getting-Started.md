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

`~lola = Pbind(\type, \sample, \sound, \break125);`

Play the sound:

`[ ~lola ].ziva.play;`

Stop the sound:

```
[ nil ].ziva.play;
// or 
Ziva.stop;
// or press CTRL+.
```

### Durations

Play the sound fast:

`[ ~lola.fast ].ziva.play;`

Other durations are `faster, fastest, slow, slower, slowest`.

For custom durations:

`[ ~lola.dur(0.628) ].ziva.play;`
 
### Loudness

Play it loud (mezzo forte):

`[ ~lola.f ].ziva.play;`

Try `ff,fff,ffff,p,pp,ppp`.

For custom loudness **(BE CAREFUL!!)**:

```
[ ~lola.amp(0.2) ].ziva.play;
// or
[ ~lola.amp(-12.dbamp) ].ziva.play;
```

### Legato

`[ ~lola.stacc ].ziva.play; // play staccato`

Try `pizz, stass, stacc, tenuto, legato, pedal`.

For custom legato:

`[ ~lola.legato(0.628) ].ziva.play;`

### Rhythms

Euclidean distribution:

`[ ~lola.faster.bj(5,8) ].ziva.play;`

Custom rhythms

`[ ~lola.faster.r([r,r,1,r,r,1,r,1,r,1,r,1].pseq) ].ziva.play;`


### Miscellaneous 

Play once:

`[ ~lola.once ].ziva.play;`

Play 3 times then stop:

`[ ~lola.once(3) ].ziva.play;`



### Sample parameters

Change the playing speed (negative rates play backwards):

`[ ~lola.rate(0.25) ].ziva.play;`

Pick 4 values from an array of rates, and sequence them endlessly:

`[ ~lola.fast.randrates(4, [-1,1,-0.5,0.5,2,-2]) ].ziva.play;`

Change the starting position (0.0 for beginning, 1.0 for end):

`[ ~lola.start(0.3) ].ziva.play;`

Slice a sample into 16 chunks, then pick 8 randomly (may be repeated) and play them in sequence endlessly:

`[ ~lola.faster.chop(8, 16) ].ziva.play;`



## Playing a synth

Besides samples, regular SynthDefs can also be played. The only condition is for them to have an envelope with `doneAction:2` (self-releasing upon finnished). If they don't the synth stack will grow and end up collapsing the server (which is also cool).

```
~nala = Pbind(\instrument, \acid);
[ ~nala ].ziva.play;
```
The [basic functions](#basic-functions) 
