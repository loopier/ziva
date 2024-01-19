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
~folda lfo: noise1(10.5, 0.01, 0.9); // used in distortion

// prepare the instruments
// deep bass with a chorus effect
~bass s: \kwbass dur: (8..16).prand octave: [3,4] fx1: \chorus;
// some ear candy using lfos in the effects' parameters
~candy s: \crisp amp: (1/(1..10).pwalk) dur: (1/(1..100).pwalk) pan: (1/(-10..10).pwalk);
~candy fx1: fbdelay(~delt, ~delfb) fx2: vcf(~co, ~res);
// shy lead
~lead s: \theremin dur: (4..8).prand degree: [3,4,5].prand legato: 1.2 octave: [6,5,6] fx1: hpf(400);
// distorted guitarish sound
~gtr s: \pulse dur: ((1..8).prand) degree: ([2,3,4].prand + [0,4,7]) atk: 0.5 rel: 0.4 legato: 1 octave: [3,4].prand amp: 0.1 fx1: nil fx2: \chorus fx3: nil;

// patch sounds to mixer
// the number in `<=.N` is the channel number in the mixer
~mixer <=.1 ~bass mix1: 0.1;
~mixer <=.2 ~candy mix2: 0.1; // adding dry signal to mix
~mixer <=.3 ~lead mix3: 0.3;

// a track with a dedicated signal chain
// a track is just an insturment without any
// sound attached to it (no `s:` or `sound:`) and with
// at least one effect attached to it
~track fx1: \reverbL;
// a track is also a mixer
// patch an instrument to the track
~track <=.1 ~gtr mix1: 0.1; //
// we can patch the other way (from right to left)
~candy =>.2 ~track;
// but then the mix has to be done separately
~track mix2: 0.3; // a bit of reverb

// we finally patch the track to the output mixer with
// yet another syntax. This only patches the source to
// the main output mixer, it cannot be used to patch anything
// to a submixer like `track`
// this is the same as writing:
// ~mixer <=.4 mix4: 0.1
~track =>>.4 0.1;

// we finally set a global effect that affects the final output and
// but first, prepare an lfo to control the wetness
~drywet lfo: sine(0.1, 0, 1);
// add the effect
~mixer fx1: hpf(70);
// and control the wetness with the lfo
~mixer wet100: ~drywet;
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
- `~bla =>>.2 0.5` shortcut of `~mixer <=.2 ~bla mix2: 0.5
- `~bla 2.=> ~mixer` is opposite of `~mixer <=.2 ~mixer` -- maybe `~bla =>.2 ~mixer`
- replicate HydraSynth's FX signal chain: `sig -> prefx -> delay -> reverb -> postfx`

