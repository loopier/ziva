## TODO

``` supercollider
// proof of concept

~da.play;
~pa.play;
~pa.pause;

// define parts
// redifining them doesn't interrupt the playing of the score (see below)
a = {
    ~da.seed(1000) s: \kwbass octave: 3 degree: [0,2,4,0,1,r].pick(8).pseq dur: (1) fx1: nil;
};

b = {
    ~pa s: \kwstring octave: [5,6].choose degree: [0,2,4,0,1,r].pick(8).pseq dur: (1/2) legato: 0.1 rel: 1.5 fx1: \reverb amp: 1
};

c = {
    ~da.seed(1000) s: \fmx octave: 3 degree: [0,2,4,0,1,r].pick(8).pseq dur: (1/1) legato: 0.1 rel: 1.5 mod11: 1 mod21: 1 mod12: 1 fx1: vcf(1400,0.8) fx2: \chorus;
    ~pa.resume s: \kwstring amp: 0.1 fx1: \reverb dur: 4 degree: [0,4,7] legato: 1 rel: 4;
};


// score parts
(
[
    a.(),
    2.bars,

    b.(),
    1.bars,

    a.(),
    b.(),
    4.bars,
].ziva
)
```

## Usage

``` supercollider
Ziva.boot;
Ziva.samples("path/to/samples/dir");
Ziva.scale = \harmonicMinor;
Ziva.bpm = 96;

// lfo's
~fb lfo: sine(0.1, 0, 1)
~filta lfo: sine(0.1, 400, 1000)
// snd synth
~za s: \fmx fb: ~fb
// fx
~fxa fx1: vcf(~filta, 0.7) fx2: \reverb w1: 0.5
// routing
~za <>> ~fxa
~fa.play;
```

##

