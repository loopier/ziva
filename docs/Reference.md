# Živa Reference

This document exposes the API available through Živa.


*Optional arguments are surrounded by `[]`*

Argument types may sometimes be noted as:
- `:i` (int)
- `:f` (float)
- `:s` (string)
- `:S` (symbol)
- `:F` (function)

*Symbols always start with ` \ `*


Regular SuperCollider code can be normally used anywhere.
    
# Basic Example

```
Ziva.boot;

(
\d1 play: [
    \acid rh: 'fa',
];
)
```

## A more elaborated example

```
Ziva.boot;
Ziva.tempo = 143;
Ziva.scale = \bhairav;

\filta lfo: sine(0.1, 300, 2500);
\filte lfo: noise2(0.1, 300, 2500);

(
\d1 play: [
    \prophet rh: bj(5,8,-1) dur: \fast deg: '0001' oct: [5] amp: \pp cutoff: \filta rq: 0.7,
];
\d2 play: [
    \acid rh: bj(5,12) dur: \fast deg: '0001' oct: 3 cutoff: \filte amp: \pp,
];
\dr play: [
    \aekick rh: 'a0a0a550' dur: \fast amp: \ff n: 1,
    \white rh: '0808' dur: \fast perc: 0.1,
];
)

\d1 fx: [\chorus2, \reverbS, \hpfS];
\d2 fx: [delay(0.3, 8), \chorus2, \reverbL, \lpfS];
```

# System methods

Methods to setup, run the live coding enviroment and get info from its current state.

Method | Args <img width=175px/> | Description
--------|------|------------
`Ziva.boot(inputChannels:2, outputChannels:2, server:Server.default, numBuffers:16, memSize:32, maxNodes:32)` | Boot the server. All arguments are optional.
`Ziva.synths`  | List available synths.
`Ziva.samples`  | List available synths. Outputs: `sampleName (numOfSamples)`.
`Ziva.sounds`  | List all available sounds (both synths and samples).
`Ziva.fx`  | List all available sound effects.
`Ziva.rh`  | List all available rhythms.
`Ziva.rhythms`  | List all available rhythms.
`Ziva.rhythm(\rhythm)` Post the values for `ryhthm`.
`Ziva.controls(\synth)` List all available controls for the given `synth`. Alternatively, use `\synth.controls`
`Ziva.loadSamples(path:s)` Load sounds files from `path`. `path` must contain subdirectories with the actual soundfiles. The subdirectories' names will be used as `sampleName`.
`Ziva.hush`  | Stop all sounds.

# Playing sounds

To play a sound:

```
\d1 play: [
    \acid rh: 'f'
]
```

This sets a track named `d1` and plays an sound named `acid` (see available sounds with `Ziva.sounds`) and sets its rhythm to `[1,1,1,1]`.


# Sound parameters

Sounds can be modified with parameters. The syntax is: `\sound_name param1: value1 ... paramN: valueN`.

Values can either be numbers (`int` or `float`), hexadecimal numbers surrounded by single-quotes (e.g. `80fa'`), symbols (e.g. `\ff`) or functions (see ###TODO: ADD LINK TO MODULATOR FUNCTIONS###).

Symbols can be either found in `Ziva.constants` or any declared LFOs. See ###TODO: ADD LINK TO LFOS###.


## `rh`

Method |  Description
--------|------------
`rh: rhythm:S` | Play a `rhythm`. Example: `rh: \tumbao`. See avilable rhythms with `Ziva.rhythms`.
`rh: Array` | Sequence the array. Example:  `rh: [1,r]`. *Note: `r` are rests, any number is a hit.*
`rh: Pattern` | Play a `pattern`. Accepts both regular SClang syntax for patterns and Živa syntax. Example: `rh: [1,r].pseq` (same as `Array` example).
`rh: Hex` | Play a rhythm written as `hex` values. Each value describes for beats in binary form. Example:`'8f'` is the same as `[1,r,r,r, 1,r,1,r].pseq`.

## `dur`

Method |  Description
--------|------------
`dur: time:if` | The `time` until next event in a sequence of events.
`dur: \fast` | Play at `2x` tempo.
`dur: \faster` | Play at `4x` tempo.
`dur: \fastest` | Play at `8x` tempo.
`dur: \ultrafast` | Play at `16x` tempo.
`dur: \ultrafaster` | Play at `32x` tempo.
`dur: \ultrafastest` | Play at `64x` tempo.
`dur: \slow` | Play at `1/2` tempo.
`dur: \slower` | Play at `1/4` tempo.
`dur: \slowest` | Play at `1/8` tempo.
`dur: \ultraslow` | Play at `1/16` tempo.
`dur: \ultraslower` | Play at `1/32` tempo.
`dur: \ultraslowest` | Play at `1/64` tempo.

## amp

Method |  Description
--------|------
`amp: amplitude:f` | Set output volume of the sound. **WARNING: IT GET'S LOUD!!! -- it defaults to 0.1; keep values below 1.0**.
`amp: lfo:S` | Set output volume of the sound with an `lfo`.
`amp: \f` | Set amp to `0.2`.
`amp: \f` | Set amp to `0.2`.
`amp: \ff` | Set amp to `0.3`.
`amp: \fff` | Set amp to `0.5`.
`amp: \ffff` | Set amp to `0.9`.
`amp: \p` | Set amp to `0.05`.
`amp: \pp` | Set amp to `0.03`.
`amp: \ppp` | Set amp to `0.02`.
`amp: \pppp` | Set amp to `0.01`.

## pan

Left-right stereo panning effect. `-1` for left, `1` for right, `0` for center.

Method |  Description
--------|------------
`pan: position:f`  | Send output to a `position`. `-1` is left, `0` is center, `1` is right;
`pan: lfo:S`  | Send output to a position with an `lfo`.
`pan: \left`  | Send output to left channel.
`pan: \right`  | Send output to right channel.
`pan: \pingpong`   | Alternate left and right panning.

## `env`

Envelope of the sound -- only works with functions.

Method | Args | Description
--------|------|------------
`perc` | `release` | Add percussive envelope with a `release` in seconds.
`ar` | `attack, release` | Add an attack-release envelope with `attack` and `release` values in seconds.
`adsr` | `attack, decay, sustain, release` | Add an ADSR envelope with `attack`, `decay` and `release` values in seconds, and `sustain` from `0` to `1`.

## `leg`

Legato time.

Method | Args | Description
--------|------|------------
`leg` | `length` | See `legato`.
`legato` | `[length]` | Hold the note for a `length` of secons. Then release. Defaults to `1.0`.
`pizz` | | Play pizzicato.
`stass` | | Play staccatissimo.
`stacc` | | Play staccato.
`pedal` | | Play pedal. Hold notes during `4` events.

# Synth parameters

Following is a list of other parameters that take either numerci values.
When a `symbol` is accepted, it can be any LFO name.
When `hex` values are accepted, they are converted to a list of decimal values an sequenced.
When a `func` is accepted, it can be either a list generator, or a pattern. See ###TODO: ADD LINK TO MODULATOR FUNCTIONS###

Method | Args | Description
--------|------|------------
`scale`  | `name:symbol` | Set scale for this instrument. To list available scales: `Scale.directory`.
`deg`  | `degree:pattern|int|float|hex` | Play the given degree `num` in the current scale. `0` is the root. Can be a pattern. If a `hex`
`note`  | `note:pattern|int|float|hex` | Play chromatic note. `0` is root at the current octave. Can be a pattern.
`midinote`  | `note:pattern|int|float|hex` | Play the MIDI note `num`.  Can be a pattern.
`freq`  | `hz:symbol|pattern|int|float|func` | Set the frequency to `hz`.  Can be a pattern.
`oct`  | `octave:int|float|hex` | Play in the given **octave**. Can be a pattern.

## Samples

Methods exclusive for samples.

Method | Args | Description
--------|------|------------
`n` | `sample:int|pattern` | Set the sample index.
`speed`  | `rate:symbol|int|float|pattern|func` | Play the sample at the given `speed`. Affects the pitch.
`tape` | `amount` | Old cassette tape effect. The greater the `amount`, the older the tape.
`chop` | `[length, chunks]` | Chop the sample in a number of `chunks`, pick a random number of chunks given by `length` and play them in sequence.

# LFOs

LFOs are created like regular tracks. In the following example we create a `sine` LFO with a frequency of `0.1`, that ranges from `150` to `2400`. This could be used to modulate a frequency:

```
\lfo1 lfo: sine(0.1, 150, 2400);
\d1 play: [ \prophet oct: 3 cutoff: \lfo1 ];
```

Method | Args | Description
--------|------|------------
`sine` | `freq min max amp phase` | Creates a sine wave that oscillates at `freq` between `min` and `max`.
`tri` | `freq min max amp phase` | Creates a triangle wave that oscillates at `freq` between `min` and `max`.
`saw` | `freq min max amp phase` | Creates a sawtooth wave that oscillates at `freq` between `min` and `max`. For ramp (downwards) values, set `min` to a higher value than `max`.
`pulse` | `freq min max amp phase` | Creates a pulse wave that oscillates at `freq` between `min` and `max`.
`noise0` | `freq min max amp phase` | Generates random values at an interval of `freq`.
`noise1` | `freq min max amp phase` | Generates linearly interpolated random values at a rate given by `freq`.
`noise2` | `freq min max amp phase` | Generates quadratically (exponential) interpolated random values at a rate given by `freq`.

# Pattern Functions

A few functions to create patterns:

Method | Args | Description
--------|------|------------
`borwn`|`min:i|f max:i|f interval:i|f`| Brown noise. Returns `Pbrown(min, max, interval).`
`white`|`min:i|f max:i|f`| White noise, random values. Returns `PWhite(min, max).`
`borwn`|`min:i|f max:i|f interval:i|f`| Brown noise. Returns `Pbrown(min, max, interval).`

# Track

Tracks have some methods that don't accept arguments.

Method | Args | Description
--------|------|------------
`controls` | | `\d1.controls` or `controls(\d1)`
`solo` | | `\d1.solo` or `solo(\d1)`
`unsolo` | | `\d1.unsolo` or `unsolo(\d1)`
`mute` | | `\d1.mute` or `mute(\d1)`
`unmute` | | `\d1.unmute` or `unmute(\d1)`

# FX - Effects

Three types of effects can be added: presets, customizable, and brand new.
The mix of flitered and original signal for a given track can be changed with values from `0` (dry) to `1` wet: 
`\d1 wet: 0.5` 

## Presets

To add effect presets to a track named `d1`, add the names of the effects in the order (left to right) in which they should be chained. To get a list of available effects, evaluate `Ziva.fx`: 
```
\d1 play: [\acid rh: '8'];
\d1 fx: [\delay, \chorus2, \reverbS]:
```

## Basic custom FX

Some effects accept custom values:
```
\d1 fx: [delay(0.3, 8), \reverbS]
```

Method | Args | Description
--------|------|------------
`delay`| `time:symbol|i|f decay:symbol|i|f` | AllpassC.
`lfp` | `cutoff:symbol|i|f resonance:symbol|i|f` | Resonant low pass filter.
`hfp` | `cutoff:symbol|i|f resonance:symbol|i|f` | Resonant high pass filter.
`moogvcf` | `cutoff:symbol|i|f resonance:symbol|i|f` | Moog Voltage Controlled Filtered.


## Advanced custom FX

New effects can be added with functions that would normally be accepted as source in a `NodeProxy.sources` slot in SuperCollider:

`\d1 fx: [{|in| AllpassC.ar(sig, 0.2, 0.2, 1)}]`

## MIDI

**TODO.**

# Patterns

Describe the order in which items in a list are processed. These can be set as 

**Syntax:** `[item1, item2, ..., itemN].paternName(... args)`

They can be chained, and embedded.

**Examples:** 

```
[0,2,4].pseq
[[0,2,4].pseq(2),2,2,[0,4].prand(4)].pseq
[0,2,4].pshuf(2).pn
```

SuperCollider pattern syntax can also be used. The list of available SC patterns is huge. A good place to start is [What else can patterns do?](https://doc.sccode.org/Tutorials/Getting-Started/16-Sequencing-with-Patterns.html).

Method | Args | Description
--------|------|------------
`pseq`  | `[repeats]` | Plays values of the list in order a number of `repeats`. Defaults to `inf`. If an item is a list, plays a chord. 
`place`  | `[repeats]` | Like `pseq`, but for items that are lists it returns one element on each iteration.
`prand`  | `[repeats]` | Chooses a random value from the list `repeats` times. Defaults to `inf`.
`pxrand`  | `[repeats]` | Like `prand`, but never repeating the same element twice in a row.
`pwrand`  | `weights [, repeats]` | Like `prand`, but the probability of each item is determined by `weights`.
`pcoin`  | `probability` | Chooses between the first and second element of the list, with a `probability` (between `0.0` and `1.0`). Lower values favours first element, higher values favour second element.
`pshuf` | `[repeats]` | Returns a shuffled version of the `list` item by item, repeating the same sequence a number of `repeats`.
`pindex`  | `pattern [, repeats]` | Choose values from the list at the index given by `pattern`.
`pser` | `[repeats, offset]` | Like `pseq`, however the repeats variable gives the number of items returned instead of the number of complete cycles
`pswitch` | `which` | Chooses elements from the list indices indicated by a `which` (can be a pattern). If the element is a itself pattern it plays it completely before moving to the next element.
`pswitch1` | `which` | Like `pswitch`, but if the element is a pattern, it plays only the current value, going through the rest of the elements before playing the next one.
`ptuple` | `[repeats]` | Play a list of patterns, reseting when the shortest one finishes. 
`ppatlace` | `[repeats, offset]` | Like `place` but with embedded patterns instead of plain values.
`pslide` | `[length:3, step:1, start:0, wrapAtEnd:true, repeats:inf]` | Plays sliding sequences of items of a given `length`. `step` sets how far from the previous segment each segment starts; it can be negative. `start` sets the first index. If `wrapAtEnd` is `true`, index wraps around either end; if `false`, the pattern stops when it goes out of bounds.
`pwalk` | `step, direction:1, startPos:0` | Randomly walk over the list of items incrementing the index by `step`; it can be a pattern. When hitting the boundaries of the list, go in the new `direction`; it can also be a pattern.
`pn` | `[repeats]` | Repeat previous pattern `repeats` times.
`pdef` | `key` | Creates a `Pdef(\key, Ppar( ...list... ))`, placing the list as the `Ppar` contents.

# List Generator Functions

Functions to create lists of values. 

## Envelope Generator Functions

Used with sound parameter `env`:

Method | Args | Description
--------|------|------------
`adsr`| `attack:f decay:f sustain:f release:f`| Returns an array of 4 values.
`ar`| `attack:f release:f`| Returns an array of 2 values.
`perc`| `attack:f decay:f sustain:f release:f`| Returns an array of 1 value.

## Other Functions

Following is a list of the available methods for lists of data. SuperCollider array methods can also be used. See [SuperCollider Array Help](https://doc.sccode.org/Classes/Array.html) 

*The variable `r` can be used inside any list to add a rest.*

Method | Args | Description
--------|------|------------
`choosen` | `number` | Creates a list of any `number` of items taken randomly from the list.
`dupand` | `number, other` | Duplicates the list a `number` of times and flattens it. Then it concatenates the `other` at the end of it.
`triand` | `other` | Triplicates the list, then it concatenates `other` at the end of it.
`anddup` | `number, other` | Like `dupand`, but adding `other` to the beginning instead of the end.
`sextine` | | Returns an array of variations of the list according to the algorithm used to compose [sextines](https://en.wikipedia.org/wiki/Sestina) in poetry.
`rhythm` | `pattern [,reverse]` | Crates a rhtythm following the `pattern`, and sequencially places the elements in the list on the hits, skipping the rests. For example: `[0,2,4].rhythm([1,1,\r,1,\r,1])` returns `[0,2,\r,4,\r,0]`. Non-rests can be anything. If `reverse` is `true`, the `pattern` is parsed backwards.
`inv` | | Rests in the list are converted to `0`, and vice-versa.
`nameOfRhythm` | `[reverse:0]` | Elements of the list are place sequencially on the hits of the rhythm. Available names are listed with `Ziva.rhythms`. Some of them are: `clave`, `rumba`, `binaneth`, `chitlins`, `cascara`, `cencerro`, `cencerru`, `montuno`, `conga`, `tumbao`, `tumbau`, `horace`, `buleria`, `nine`, `eleven`, `tonebank`, `tracatrin`, `tracatron`, `tracatrun` 
`bj` | `hits, beats [, offset, reverse]` | Like `rhythm`, but with [euclidean rhythms](https://en.wikipedia.org/wiki/Euclidean_rhythm), placing items on any number of `hits` over a number of `beats`, and `0`s elsewhere. 
`bjr` | `hits, beats [, offset, reverse]` | Like `bj`, but adding `\r` (rests) instead of `0`s.
`bj2` | `hits, beats [, offset, reverse]` | Sets a number of `hits`, whose durations add up to any number of `beats`.
`!!` | `repeats` | Like `[...].pseq(repeats)`. Usage: `[0,2,4]!!4`.
`??` | `repeats` | Like `[...].prand(repeats)`. Usage: `[0,2,4]??4`.
`?!` | `size [, repeats:inf]` | Like `[...].choosen(size).pseq(repeats)`. Usage: `[0,2,4]?!4`.
`ziva` | | Instruments in the list will be played in parallel (see [Basic Example](#basic-example) above). Like `Pseq(\ziva, Ppar( ...list... )).play.quant_(1)`.

