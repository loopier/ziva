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
Ziva.boot; s.plotTree;
p = ProxySpace.push(s).quant_(1);
~sa.play.seed(12) s: \fmx octave: 5 degree: arp(1).add(r).choosen(8).debug.pseq dur: (1/4) mod11: 0.3 amp2: 1
~se.play s: \pulse legato: 0.1 octave: 3;
// patch
~alo <>> ~ta
~bla <>> ~te
~ta.unmap(\in)
~te.unmap(\in)
```

##

