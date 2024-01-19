## Ideas

Use a `~mixer` nodeproxy as final node.
Add other proxies to first slots ([1..99]) using a custom operator with an adverb (`=>.3`?) 
Add fx to higher-numbered slots ([100..])

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

(
// prepare lfos
~co lfo: noise0(1, 400, 10000);
~res lfo: noise0(1, 0.4, 0.85);
~delt lfo: noise0(1, 0.04, 0.3);
~delfb lfo: noise0(1, 0.4, 0.95);

// set a bass with an effect
~bla s: \bass fx1: \chours;
// set some ear candy using lfos in effects parameters
~alo.stop s: \crisp amp: (1/(1..10).pwalk) dur: (1/(1..100).pwalk) pan: (1/(-10..10).pwalk) fx1: fbdelay(~delt, 0.9) fx2: vcf(~co, ~res);

// set up a mixer with a global effect
~mixer.play fx20: fbdelay(0.3, 0.9);

// patch ~bla to mixer slot 1
~mixer =>.1 ~bla;
// patch ~alo to mixer slot 2
~mixer =>.2 ~alo;

// mix
~mixer mix1: 0.5;
~mixer mix2: 1.0;
)
```

