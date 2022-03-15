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

Assing a sample sound to a variable:

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

Different variations of the same sound can be played simultaneously — forget the functions for now, we'll get to them shortly:

```
(
[
    ~lola.faster.bj(3,8).pan(1),
    ~lola.faster.bj(5,8).rate(1/2).pan(-1),
].ziva.play;
)
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

Pan the audio to Left and Right:

```
[ ~lola.pan(-1) ].ziva.play; // left
[ ~lola.pan(-0.5) ].ziva.play; 
[ ~lola.pan(0) ].ziva.play; // center -- default
[ ~lola.pan(1) ].ziva.play; // right
```
Alternate between left and right

`[ ~lola.pingpong ].ziva.play;`

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

To get a list of currently loaded synths:

`Ziva.synths;`

```
~nala = Pbind(\instrument, \acid);
[ ~nala ].ziva.play;
```

The [basic functions](#basic-functions) also work for synths except —obviously— those related to samples. 

In addition there are other functions useful only in synths. For example, playing the root note (degree) of the scale:

`[ ~nala.deg(0) ].ziva.play;`

or a chord

`[ ~nala.deg([0,2,4]) ].ziva.play;`

change the octave (default is 5):

`[ ~nala.oct(4) ].ziva.play;`

Change a parameter specific to this synth:

`[ ~nala.cutoff(2000) ].ziva.play;`

Other synths might have other parameters that can be modified just using their name as a function call: `~nala.nameOfYourSynthArg(value)`.

## Sequencing function parameters

Most parameters for most functions can be sequenced, which is a fancy name for automating changes algorithimacally. In SC this is done with Patterns. There's a **huge** amount of patterns, but a lot can be achieved with a few. To create a pattern just type in an array followed by the pattern you want to apply to it.

`Pseq` sequences values in the array one after the other for ever if not specified otherwise:

`[ ~nala.deg([0,2,4].pseq) ].ziva.play;`

Regular SC syntax can be used — *not all SC patterns have their equivalent in Živa, but all of them can be used with regular SC syntax*:




`Prand` picks one value on each event (every "hit"):

`[ ~nala.deg([0,2,4].prand) ].ziva.play;`

The variable `r` can be used for rests in patterns:

`[ ~nala.deg([0,2,4,r].prand) ].ziva.play;`

You can nest patterns within patterns:

`[ ~nala.faster.deg([0,2,r, [6,5,4].pseq(1)].prand) ].ziva.play;`

Other interesting patterns might be `Place, Pstutter, Pshuf, Pxrand, Pwrand, Pslide, Pwalk, ...`. See [Pattern Guide](https://doc.sccode.org/Tutorials/A-Practical-Guide/PG_02_Basic_Vocabulary.html)  

## Effects

Živa comes with some effects that can be applied to sounds. To list them:

`Ziva.fx;`

Effects can only be applied to tracks, not to sounds.  A track is defined with:

`Ziva.track(0); // this is a track without any effects`

To apply an effect:

`Ziva.track(0, \delay);`

Many effects can be applied, order matters:

```
Ziva.track(0, \delay, \reverbL); 
// is not the same as
Ziva.track(0, \reverbL, \delay); 
```

To add effects to the playing sounds:

```
(
[
    ~nala >> 0 // point it to the track number
].ziva.play;
)
```

To remove effects from a sound;

```
(
[
    ~nala,      // remove the pointer
                // or
    ~nala >> 1, // point it to a track without effects
].ziva.play;
)
```

To remove effects from a track (this will also remove them from the sounds pointing to it):

`Ziva.track(0);`
