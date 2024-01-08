## TODO

``` supercollider
// proof of concept

// lfo's
~fb lfo: sine(0.1, 0, 1)
~filta lfo: sine(0.1, 400, 1000)
// snd synth
~za s: \fmx fb: ~fb
// fx
~fxa fx1: vcf(~filta, 0.7) fx2: \reverb w1: 0.5
// routing
~za <>> ~fxa
```

## Usage

``` supercollider
Ziva.boot;
Ziva.samples;

// create sounds
~alo s: \acid
~alo s: \acid degree: [0,4].pseq sustain: Pwhite(0.1) dur: (1/2) octave: 3 pan: [-1,1].pseq
~bla s: \piezo
~bla s: \piezo amp: 0.1 n: (..7).choosen(8).pseq dur: (1/4) <>> ~ta
~bla.stop
// create fx
~ta fx: \bla;
~te fx: \ble;
~ta.play;
~te.play;
~ta[1] = \filter -> Ziva.fxDict[\gverb];
~te[1] = \filter -> Ziva.fxDict[\delay];
// patch
~alo <>> ~ta
~bla <>> ~te
~ta.unmap(\in)
~te.unmap(\in)
```
