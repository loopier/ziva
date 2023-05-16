# Contents
<!-- markdown-toc start - Don't edit this section. Run M-x markdown-toc-refresh-toc -->
**Table of Contents**

- [Contents](#contents)
- [Intro](#intro)
    - [Basic Example](#basic-example)
    - [A more elaborated example](#a-more-elaborated-example)
- [System methods](#system-methods)
- [Playing sounds](#playing-sounds)
- [Sound parameters](#sound-parameters)
    - [-](#-)
    - [Duration](#duration)
    - [Volume (Amplitude)](#volume-amplitude)
    - [Panning](#panning)
    - [Envelope](#envelope)
    - [Legato](#legato)
- [Synth parameters](#synth-parameters)
    - [Samples](#samples)
- [LFOs](#lfos)
- [Track](#track)
- [FX - Effects](#fx---effects)
    - [Presets](#presets)
    - [Basic custom FX](#basic-custom-fx)
    - [Functions - Advanced custom FX](#functions---advanced-custom-fx)
    - [FX mix (dry - wet)](#fx-mix-dry---wet)
- [MIDI](#midi)
- [Patterns](#patterns)
- [List Generator Functions](#list-generator-functions)
- [Pattern Functions](#pattern-functions)

<!-- markdown-toc end -->

# Intro

This document exposes the API available through Živa.


*Optional arguments are surrounded by `[]`*

Argument types may sometimes be noted as:
- `:i` (int)
- `:f` (float)
- `:s` (string)
- `:S` (symbol)
- `:F` (function)
- `:P` (pattern)
- `:H` (hex)
- `:L` (LFO)

*Symbols always start with ` \ `*


Regular SuperCollider code can be normally used anywhere.
    
## Basic Example

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

Values can either be numbers (`int` or `float`), hexadecimal numbers surrounded by single-quotes (e.g. `80fa'`), symbols (e.g. `\ff` or an [LFO](#lfos)) or functions (see [List Generator Functions](#list-generator-functions), [Pattern Functions](#pattern-functions))

Symbols can be either found in `Ziva.constants` or any declared LFOs. See [LFOs](lfos).


### Rhythm

Method |  Description
--------|------------
`rh: rhythm:S` | Play a `rhythm`. Example: `rh: \tumbao`. See avilable rhythms with `Ziva.rhythms`.
`rh: Array` | Sequence the array. Example:  `rh: [1,r]`. *Note: `r` are rests, any number is a hit.*
`rh: Pattern` | Play a `pattern`. Accepts both regular SClang syntax for patterns and Živa syntax. Example: `rh: [1,r].pseq` (same as `Array` example).
`rh: Hex` | Play a rhythm written as `hex` values. Each value describes for beats in binary form. Example:`'8f'` is the same as `[1,r,r,r, 1,r,1,r].pseq`.

### Duration

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

### Volume (Amplitude)

Method |  Description
--------|------
`amp: amplitude:ifPL` | Set output volume of the sound. **WARNING: IT GET'S LOUD!!! -- it defaults to 0.1; keep values below 1.0**.
`amp: \f` | Set amp to `0.2`.
`amp: \f` | Set amp to `0.2`.
`amp: \ff` | Set amp to `0.3`.
`amp: \fff` | Set amp to `0.5`.
`amp: \ffff` | Set amp to `0.9`.
`amp: \p` | Set amp to `0.05`.
`amp: \pp` | Set amp to `0.03`.
`amp: \ppp` | Set amp to `0.02`.
`amp: \pppp` | Set amp to `0.01`.

### Panning

Left-right stereo panning effect. `-1` for left, `1` for right, `0` for center.

Method |  Description
--------|------------
`pan: position:ifPL`  | Send output to a `position`. `-1` is left, `0` is center, `1` is right;
`pan: \left`  | Send output to left channel.
`pan: \right`  | Send output to right channel.
`pan: \pingpong`   | Alternate left and right panning.

### Envelope

Envelope of the sound.

Method |  Description
--------|------------
`env: perc(release:f)` | Add percussive envelope with a `release` in seconds.
`env: ar(attack:f, release:f)` | Add an attack-release envelope with `attack` and `release` values in seconds.
`env: adsr(attack:f, decay:f, sustain:f, release:f)` | Add an ADSR envelope with `attack`, `decay` and `release` values in seconds, and `sustain` from `0` to `1`.
`perc: release:ifPL` | Add percussive envelope with a `release` in seconds.

### Legato

Legato time (sustain time).

Method |  Description
--------|------------
`leg: length:ifPL` | See `legato`.
`leg: length:ifPL` | Hold the note for a `length` of seconds. Then release. Defaults to `1.0`.
`leg: \pizz` | Play pizzicato.
`leg: \stass` | Play staccatissimo.
`leg: \stacc` | Play staccato.
`leg: \pedal` | Play pedal. Hold notes during `4` events.

# Synth parameters

Following is a list of other parameters that take either numerci values.
When a `symbol` is accepted, it can be any [LFO](#lfos) name.
When `hex` values are accepted, they are converted to a list of decimal values an sequenced.
When a `func` is accepted, it can be either a list generator, or a pattern. See [List Generator Functions](#list-generator-functions) and [Pattern Functions](#pattern-functions)

Method |  Description
--------|------------
`scale: name:S` | Set scale for this instrument. To list available scales: `Scale.directory`.
`deg: degree:if` | Play the given `degree` in the current scale. `0` is the root. Example: `deg: 0`.
`deg: degrees:P` | Play a pattern on a list of `degrees` in the current scale. `0` is the root. Example: `deg: [0,2,4].pseq`.
`deg: degrees:H` | Play a sequence of `degrees` in the scale, described as hex values. `0` is the root. Example: `deg: '024'`.
`note: note:if` | Play chromatic note. `0` is root at the chromatic octave. See `deg` for examples.
`note: notes:P` | Play a pattern on a list of `notes` in the chromatic scale. `0` is the root. Example: `note: [0,2,4].pseq`.
`note: notes:H` | Play a sequence of chromatic `notes` described as hex values. `0` is the root. Example: `note: '012'`.
`midinote: note:if` | Play the MIDI `note`. Example: `midinote: 60`.
`midinote: notes:P` | Play a pattern of MIDI `notes`. Example: `midinote: [60,63,67].pseq`.
`freq: hz:if` | Set the frequency to `hz`. Example: `freq: 440`.
`freq: frequencies:P` | Play a pattern of the `frequencies` in hz. Example: `freq: [440, 220, 880].pseq`.
`freq: lfo:S` | Change the frequency with and `lfo`. Example: `freq: \lfo1` -- See LFOs.
`oct: octave:if` | Play in the given `octave`. Example: `oct: 3`. Defaults to `5`.
`oct: octaves:P` | Play a pattern with the given `octaves`. Example: `oct: [3,4,5].pseq`.

Additionally, any synth parameters declared in the SynthDef, may be used. Any newly created synth parameters will also be available, as long as the SynthDef is loaded in the server. To see the available parameters for a given synth, use:
```
Ziva.controls(\synthName);
\\ or
\synthName.controls;
```
Then use it as a regular sound parameter:

Method |  Description
--------|------------
`paramName: value` | The parameter type is declared in the SynthDef. For numeric values, usually and LFO may be used. Example: `width: 0.5`. **WARNING: it only works with single-value parameters.**

## Samples

Methods exclusive for samples.

Method |  Description
--------|------------
`n: sample:iP` | Set the sample index. Example: `n: 0`; `n: [0,1,2].pseq`
`speed: rate:ifPL` | Play the sample at the given `rate`. Affects the pitch. Example: `speed: 0.5`; `n: [0.2,0.5,1,2].prand`; `n: \lfo1` -- see LFOs.
`tape: amount:f` | Old cassette tape effect. The greater the `amount`, the older the tape.
`start: point:fP` | Chop the sample in a number of `chunks`, pick a random number of chunks given by `length` and play them in sequence.

# LFOs

LFOs are created like regular tracks. In the following example we create a `sine` LFO with a frequency of `0.1`, that ranges from `150` to `2400`. This could be used to modulate a frequency:

```
\lfo1 lfo: sine(0.1, 150, 2400);
\d1 play: [ \prophet oct: 3 cutoff: \lfo1 ];
```

Method |  Description
--------|------------
`lfo: sine(freq, min, max, amp, phase)` | Creates a sine wave that oscillates at `freq` between `min` and `max`. Example: `\lfo1 lfo: sine(0.2, 200 400)`
`lfo: tri(freq, min, max, amp, phase)` | Creates a triangle wave that oscillates at `freq` between `min` and `max`. Example: `\lfo1 lfo: tri(0.2, 200 400)`
`lfo: saw(freq, min, max, amp, phase)` | Creates a sawtooth wave that oscillates at `freq` between `min` and `max`. For ramp (downwards) values, set `min` to a higher value than `max`. Example: `\lfo1 lfo: saw(0.2, 200 400)`
`lfo: pulse(freq, min max amp phase)` | Creates a pulse wave that oscillates at `freq` between `min` and `max`. Example: `\lfo1 lfo: pulse(0.2, 200 400)`
`lfo: noise0(freq, min max amp phase)` | Generates random values at an interval of `freq`. Example: `\lfo1 lfo: noise0(0.2, 200 400)`
`lfo: noise1(freq, min, max, amp, phase)` | Generates linearly interpolated random values at a rate given by `freq`. Example: `\lfo1 lfo: noise1(0.2, 200 400)`
`lfo: noise2(freq, min, max, amp, phase)` | Generates quadratically (exponential) interpolated random values at a rate given by `freq`. Example: `\lfo1 lfo: noise2(0.2, 200 400)`

# Track

Tracks have some methods that don't accept arguments. The syntax is:
```
\name.methodName
\\ or
methodName(\name)
```

Method |  Description
--------|------------
`.controls` | Example: `\acid.controls` or `controls(\acid)`.
`.solo` | Example: `\acid.solo` or `solo(\acid)`.
`.unsolo` | Example: `\acid.unsolo` or `unsolo(\acid)`.
`.mute` | Example: `\acid.mute` or `mute(\acid)`.
`.unmute` | Example: `\acid.unmute` or `unmute(\acid)`.

# FX - Effects

Three types of effects can be added: 
- presets
- customizable
- functions

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

The arguments can accept LFOs.

Method | Args | Description
--------|------|------------
`delay( time:ifL, decay:ifL )` | AllpassC.
`lfp( cutoff:ifL, resonance:ifL )` | Resonant low pass filter.
`hfp( cutoff:ifL, resonance:ifL )` | Resonant high pass filter.
`moogvcf( cutoff:ifL, resonance:ifL )` | Moog Voltage Controlled Filtered.


## Functions - Advanced custom FX

New effects can be added with functions that would normally be accepted as source in a `NodeProxy.sources` slot in SuperCollider:

```
\d1 fx: [{|in| AllpassC.ar(in, 0.2, 0.2, 1)}]
```

The previous code is equivalent to: 
```
Ndef(\d1)[1] = \filter -> {|in| AllpassC.ar(in, 0.2, 0.2, 1)}
```

## FX mix (dry - wet)

The mix of flitered and original signal for a given track can be changed with values from `0` (dry) to `1` wet. For example: 
```
(
\d1 play: [
    \acid rh: '8',
    \saw dur: \fast leg: \pizz
];
)
\d1 fx: [delay(0.3, 8), \reverbS]
\d1 wet: 0.5 \\ <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< set the mix
```

# MIDI

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


# Pattern Functions

A few functions to create patterns:

Method | Args | Description
--------|------|------------
`brown(min:if, max:if, interval:if)`| Brown noise. Returns `Pbrown(min, max, interval).`
`white(min:if, max:if)`| White noise, random values. Returns `PWhite(min, max).`
`!!` | `repeats` | Like `[...].pseq(repeats)`. Usage: `[0,2,4]!!4`.
`?!` | `size [, repeats:inf]` | Like `[...].choosen(size).pseq(repeats)`. Usage: `[0,2,4]?!4`.
