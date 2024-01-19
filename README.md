# Živa

Syntax sugar for live coding in supercollider.

## Usage

``` supercollider
Ziva.boot;
Ziva.samples("path/to/samples/dir");
Ziva.scale = \harmonicMinor;
Ziva.bpm = 96;

(
// prepare lfos
~co lfo: noise0(1, 400, 10000);
~res lfo: noise0(1, 0.4, 0.85);
~delt lfo: noise0(1, 0.04, 0.3);
~delfb lfo: noise0(1, 0.4, 0.95);

// set a bass with an effect
~bla s: \bass degree: [0,4,7,0,4,7,0,4,7,4].pseq legato: 0.1 rel: 0.4 dur: (1/4) fx1: \chorus;
// set some ear candy using lfos in effects parameters
~alo s: \crisp amp: (1/(1..10).pwalk) dur: (1/(1..100).pwalk) pan: (1/(-10..10).pwalk);
~alo fx1: fbdelay(~delt, ~delfb) fx2: vcf(~co, ~res);

// set up a mixer with a global effect
~mixer fx20: \reverb;

// patch ~bla to mixer slot 1
~mixer =>.1 ~bla;
// patch ~alo to mixer slot 2
~mixer =>.2 ~alo;

// mix
~mixer mix1: 0.5;
~mixer mix2: 0.9;

// control dry wet reverb with an lfo
~drywet lfo: sine(0.1, 0, 1);
~mixer wet20: ~drywet;
)
```

