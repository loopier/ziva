# Živa Reference

This document exposes the API available through Živa.


*Optional arguments are surrounded by `[]`*

*Symbols always start with ` \ `*

Regular SuperCollider code can be normally used anywhere.
    
# Basic Example

```
Ziva.boot;
~acid = Psynth(\acid);
~k = Psample(\aekick);
Ziva.track(0, \chorus2, \reverbS);
(
[
    ~acid.faster.bj(5,8,1).randpan >> 0,
    ~acid.faster.bj(3,8).oct(3),
    ~k,
    nil
].ziva;
)
```

## A more elaborated example

```
Ziva.boot;
~acid = Psynth(\acid);
~k = Psample(\aekick);
Ziva.track(0, \chorus2);
~lfo = Ziva.lfo(1, \sine, 0.1, 300, 3000);
(
[
    ~acid.f.fast.stacc.bj(5,8).oct([5,6].prand).deg([0,2,4].pseq).randpan,
    ~acid.p.fast.oct(3).cutoff(~lfo) >> 0,
    ~k.f.n([2,r,0,0].pseq).bpm(128),
    nil
].ziva;
)
```

# System methods

Methods to setup, run the live coding enviroment and get info from its current state.

Method | Args <img width=175px/> | Description
--------|------|------------
`boot`  | `[inputChannels, outputChannels,`<br> `server, numBuffers, `<br> `memSize, maxNodes]` | Boot the server with optional parameteres.
`synths`  |  | List available synths.
`samples`  |  | List available synths. Outputs: `sampleName (numOfSamples)`.
`sounds`  |  | List all available sounds (both synths and samples).
`fx`  |  | List all available sound effects.
`rh`  |  | List all available rhythms.
`rhythms`  |  | List all available rhythms.
`rhythm`  | `name` | List all available rhythms.
`control`  | `synth` | List all available controls for the given `synth`.
`loadSamples`  | `path` | Load sounds files from `path`. `path` must contain subdirectories with the actual soundfiles. The subdirectories' names will be used as `sampleName`.
`stop`  |  | Stop all sounds.
`lfo`  | `index, wave, freq, min, max` | Returns an LFO that can be used as parameter in sound methods. `wave` can be: `sine, saw, pulse, tri, noise0, noise1, noise2`. `noise0` is with step interpolation, `noise1` is linear, and `noise2` is exponential. `min` and `max` set the range of the output value. They default to `0` and `1` respectiveley. 
`track`  | `trackNameOrNumber [, effect1, effect2, ... , effectN]` | Create an effects bus with the list of `effectX`. Order matters. Available `effect` names are listed with `Ziva.fx`, and they must be symbols (with leading ` \ `, as in `\reverb`)

# Instrument methods

Methods to use with instruments. They can be chained as in `~acid.fast.bj(3,5).pinpong`.

**Syntax:** `~instrumentName.method1(... args)[.method2(... args)...methodN(... args)]`

Method | Args | Description
--------|------|------------
`instrument`  | `name` | Set instrument. Must be a symbol (with leading ` \ `).
`ins`  | `name` | Set instrument. Must be a symbol (with leading ` \ `).
`i`  | `name` | Set instrument. Must be a symbol (with leading ` \ `).
`fast` | | Play at `2x` tempo.
`faster` | | Play at `4x` tempo.
`fastest` | | Play at `8x` tempo.
`slow` | | Play at `1/2` tempo.
`slower` | | Play at `1/4` tempo.
`slowest` | | Play at `1/8` tempo.
`ultraslow` | | Play at `1/16` tempo.
`ultraslower` | | Play at `1/32` tempo.
`ultraslowest` | | Play at `1/64` tempo.
`amp` | `min [, max]` | Play random amplitude between `min` and `max`. If `max` is ommited, it will play at even `min` value. **WARNING: It gets loud! Never go over `1.0`.**
`f` | | Set amp to `0.3`.
`ff` | | Set amp to `0.5`.
`fff` | | Set amp to `1.0`.
`ffff` | | Set amp to `2.0`.
`p` | | Set amp to `0.05`.
`pp` | | Set amp to `0.02`.
`ppp` | | Set amp to `0.01`.
`bramp` | | Brownian distribution random amplitude between `0.1` and `1.0`.
`fadin` | | Increase amplitude at each event by `1/16`.
`perc` | `release` | Add percussive envelope with a `release` in seconds.
`ar` | `attack, release` | Add an attack-release envelope with `attack` and `release` values in seconds.
`adsr` | `attack, decay, sustain, release` | Add an ADSR envelope with `attack`, `decay` and `release` values in seconds, and `sustain` from `0` to `1`.
`r` | `pattern` | Play sound on anything but `\r`. E.g.: `~acid.r([1, \r, \r, 1, 1].pseq)`.
`bj` | `hits, beats [, scramble, sort, reverse]` | See `bjorklund`.
`bjorklund` | `hits, beats [, scramble, sort, reverse]` | Euclidean rhythm generator. It plays an evenly distributed number of `hits` along a number of `beats`. Rests and hits can be shuffled if `scramble` is set to `true`. Or sorted if `sort` is set to `true` (first rests, then hits). If `reverse` is set to `true`, the pattern will be played backwards.
`upbeat` | | Play pattern every other note, starting with a rest.
`pan`  | `num|pattern` | Left-right stereo panning effect. `-1` for left, `1` for right, `0` for center.
`left`  | | Send output to left channel.
`right`  | | Send output to right channel.
`pingpong`  |  | Alternate left and right panning.
`randpan`  |  | On every event, set stereo panning to a random value between `-1` and `1`.
`leg` | `length` | See `legato`.
`legato` | `[length]` | Hold the note for a `length` of secons. Then release. Defaults to `1.0`.
`pizz` | | Play pizzicato.
`stass` | | Play staccatissimo.
`stacc` | | Play staccato.
`pedal` | | Play pedal. Hold notes during `4` events.
`once` | `times` | Play for `times` more events, then stop.
`bpm`  | `beats` | Set the tempo to `beats` per minute. **WARNING: Affects ALL playing instruments.**


## Synths

Methods exclusive for synths.

If the method does not exist, it will be passed on as synth argument.

Method | Args | Description
--------|------|------------
`>>`  | `track` | Send output to effects `track` (set with `Ziva.track(\nameOrNum)`).
`scale`  | `name` | Set scale for this instrument. To list available scales: `Scale.directory`.
`deg`  | `num` | Play the given degree `num` in the current scale. `0` is the root. Can be a pattern.
`note`  | `num` | Play chromatic note. `0` is root at the current octave. Can be a pattern.
`midinote`  | `num` | Play the MIDI note `num`.  Can be a pattern.
`freq`  | `hz` | Set the frequency to `hz`.  Can be a pattern.
`oct`  | `num` | Play in the given **octave**. Can be a pattern.
`lowest` | | Play at octave 2.
`lower` | | Play at octave 3.
`low` | | Play at octave 4.
`high` | | Play at octave 6.
`higher` | | Play at octave 7.
`highest` | | Play at octave 8.
`midinote`  | `num|pattern` | Play MIDI note. 
`fm`  | `track, modulationAmount` | Get input from effects `track` with an amplitude of `modulationAmount`. *This is usable only with synths that have an input bus parameter named `\in`.*

## Samples

Methods exclusive for samples.

Method | Args | Description
--------|------|------------
`n` | `num|pattern` | Set the sample index.
`sound` | `name` | Set the sample collection to be played.
`speed`  | `number|pattern` | Play the sample at the given `speed`. Affects the pitch.
`randspeeds`  | `[length, speeds]` | Play the sample with a sequence of `length` random values from `speeds`. Defaults to a `length` of `8` speeds from `[-1,1,-0.5,0.5,2,-2]`.
`tape` | `amount` | Old cassette tape effect. The greater the `amount`, the older the tape.
`chop` | `[length, chunks]` | Chop the sample in a number of `chunks`, pick a random number of chunks given by `length` and play them in sequence.

## MIDI

Methods exclusive of midi.

Method | Args | Description
--------|------|------------
`cc`  | `cc, value` | Send control `value` for number `cc`.

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

# Lists

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
`!!` | `repeats` | Like `[...].pseq(repeats)`.
`??` | `repeats` | Like `[...].prand(repeats)`.
`?!` | `size [, repeats:inf]` | Like `[...].choosen(size).pseq(repeats)`.
`ziva` | | Instruments in the list will be played in parallel (see [Basic Example](#basic-example) above). Like `Pseq(\ziva, Ppar( ...list... )).play.quant_(1)`.

