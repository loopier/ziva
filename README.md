# Živa

Syntax sugar for live coding in supercollider.

## Usage

``` supercollider
// evaluate this first
Ziva.boot;
(
Ziva.samples("path/to/samples/dir");
Ziva.listSamples;
Ziva.synths;
Ziva.scale = \harmonicMinor;
Ziva.bpm = 96;
// list synth controls for the \pulse synth
Ziva.controls(\pulse);

// random wave lfos
~co lfo: noise0(1, 400, 10000);
~res lfo: noise0(1, 0.4, 0.85);
~delt lfo: noise0(1, 0.04, 0.3);
~delfb lfo: noise0(1, 0.4, 0.95);
// distortion lfo to add wabble
~folda lfo: noise1(10.5, 0.01, 0.9);

// deep bass with an effect
~bass s: \kwbass dur: (8..16).prand octave: [3,4] fx1: \chorus;

// some ear candy using lfos in the effects' parameters
~candy s: \crisp amp: (1/(1..10).pwalk) dur: (1/(1..100).pwalk) pan: (1/(-10..10).pwalk);
~candy fx1: fbdelay(~delt, ~delfb) fx2: vcf(~co, ~res);

// shy lead
~lead s: \theremin dur: (4..8).prand degree: [3,4,5].prand legato: 1.2 octave: [6,6,6] fx1: hpf(400);

// distorted guitarish sound
~gtr s: \pulse dur: ((1..8).prand) degree: ([2,3,4].prand + [0,4,7]) legato: [0.4,1].prand octave: [4,3,5].prand amp: 1 fx1: fold(~folda) fx2: \chorus;

// set a track for dedicated signal chain
// this is later sent to the mixer
~track fx1: nil fx2: \reverbL;
~track <=.1 ~gtr mix1: 0.1;
~track <=.2 ~candy mix2: 0.3; // a bit of reverb

// set up a mixer with a global effect
~mixer fx1: nil;
~mixer fx2: limit(1);

// patch sounds to mixer
//
~mixer <=.1 ~bass mix1: 0.3;
~mixer <=.2 ~candy mix2: 0.4; // adding dry signal to mix with the reverberated one
~mixer <=.3 ~lead mix3: 0.3;
~mixer <=.4 ~track mix4: 0.5;

// set a global effect that affects the final output
// control dry wet hpf with an lfo
~drywet lfo: sine(0.1, 0, 1);
~mixer fx100: hpf(800);
~mixer wet100: ~drywet;
)
```

