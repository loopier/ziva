# Živa

Syntax sugar for live coding in supercollider.

## Usage

``` supercollider
// first of all boot the server
Ziva.boot;

// load samples - no samples by default
Ziva.samples("/path/to/your/samples/dir");
// list samples
Ziva.listSamples;
// check the current BPM
Ziva.bpm;
// set the BPM - defaults to random value on every boot
Ziva.bpm = 96;
// set scale - defaults to \major
Ziva.scale = \minor
// set the root (0 = C, 1 = C#, .. 11 = B) - defaults to 0 (C)
Ziva.root = 2

// list synth sounds
Ziva.synths;
// get list of controls of a synth
Ziva.controls(\pulse);

(
// random wave lfos
~co lfo: noise2(1, 400, 10000); // used in filter cutoff
~res lfo: noise2(1, 0.4, 0.85); // used in filter resonance q
~delt lfo: noise2(1, 0.04, 0.3); // used in delay time
~delfb lfo: noise2(1, 0.4, 0.95); // used in delay feedback
// distortion lfo to add wabble
~folda lfo: noise1(7, 0.1, 0.9); // used in distortion

// prepare the instruments
// deep bass with a chorus effect
~bass s: \kwbass dur: (8..16).prand octave: [3,4] fx1: \chorus;
// some ear candy using lfos in the effects' parameters
~candy s: \crisp amp: (1/(1..10).pwalk) dur: (1/(1..100).pwalk) pan: (1/(-10..10).pwalk);
~candy fx1: fbdelay(~delt, ~delfb) fx2: vcf(~co, ~res);
// shy lead
~lead s: \theremin dur: (4..8).prand degree: [3,4,5].prand legato: 1.2 octave: [6,5,6] fx1: hpf(400);
// distorted guitarish sound
~gtr s: \pulse dur: ((1..8).prand) degree: ([2,3,4,5].prand + [0,4,7]) atk: 0.5 rel: 0.4 legato: 1 octave: [4,5].prand amp: 1 fx1: fold(~folda) fx2: \chorus fx3: nil;

// patch sounds to mixer
// the number in `>>>.N` is the channel number in the mixer
// the final value is the mix level
~bass >>>.1 0.2;
~candy >>>.2 0.2;
~lead >>>.3 0.4;

// a track with a dedicated signal chain
// a track is just an insturment without any
// sound attached to it (no `s:` or `sound:`) and with
// at least one effect attached to it
~track fx1: \reverbL;
// a track is also a mixer
// patch an instrument to the track and set the mix level
~gtr =>.1 ~track mix1: 0.2;

// we finally patch the track with reverb to the output mixer with
~track >>>.4 0.1;

// set a global effect that affects the final output
// add the effect
~mixer fx1: hpf(70);
// an lfo to control the wetness
~drywet lfo: sine(0.1, 0, 1);
// and control the wetness with the lfo
~mixer wet1: ~drywet;
)

// stop everything instantly
Ziva.hush;

// or fade out in 5 seconds
Ziva.hush(5);
```
## TODO

## Ideas

- change `mixN` to `mix.N`
- change `fxN` to `fx.N`
- when creating a new ndef, automatically patch it to the next available `~mixer` slot.
- `mix(2, 0.5)` or `2.mix(0.5)` or `0.5.mix(2)` is the same as `~mixer mix2: 0.5`
- add `mousex|y(min, max)` to control parameters with the mouse
- add `midifighter(CC)` or `mf(CC)` to control parameters with MidiFighter
- `~bla >>>.2 0.5` shortcut of `~mixer <=.2 ~bla mix2: 0.5
- `~bla 2.=> ~mixer` is opposite of `~mixer <=.2 ~mixer` -- maybe `~bla =>.2 ~mixer`
- replicate HydraSynth's FX signal chain: `sig -> prefx -> delay -> reverb -> postfx`

