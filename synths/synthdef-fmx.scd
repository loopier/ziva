(
// 6-op FM synth (DX7-like) with envelope to be used with Pbind with EG levels and rates.
//
// Parameters affecting the overall synth
// \amp     Float    Overall synth amplitude
// \freq    Float    Overall synth frequency
// \attackTime       |
// \decayTime        | ADSR envelope
// \sustainLevel     |
// \releaseTime      |
//
// Parameters for each of the operators.  'N' represents the operator index as
// in \amp1 or \ratio1.
// \ampN    Float    Output level of each operator (N = operator index)
// \levelN  Float    Determines the max value of the  amplitude or how much it can modulate.
// Each operator has a 4-stage envelope.  Values can be set for each of the stage's level and rate.
// Notice that 'rate' is 1/time, so higher values mean faster times.
// \eglevelNX  Float    Value for each of the envelope levels. N = operator index, X = envelope stage.
//                      E.g. \eglevel13 is setting the value of the 3rd stage of the first operator's envelope.
// \egrateNX   Float    Value for each of the envelope rates. N = operator index, X = envelope stage.
SynthDef(\fmx, {
	var amp = \amp.kr(1);
	var spread = \spread.kr(0.8);
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var gate = \gate.kr(1);
	var freq = \freq.kr(440);
	var dur = \dur.kr(1);
    var legato = \legato.kr(1);
    var attack = \atk.kr(0.01);
    var decay = \dec.kr(0.3);
    var sustain = \sus.kr(1.0);
    var release = \rel.kr(0.01);
	var env = EnvGen.ar(Env.adsr(attack, decay, sustain, release), gate, timeScale: dur, doneAction:2);
    // operator levels are independent of their amplitudes. They can be at full level but
    // muted by the \amp parameter.
	// var levels = Array.fill(6, { |i| (\level++(i+1)).asSymbol.kr(1)});
    // on startup first operator's amp is 1, the rest are 0 -- otherwise we'd hear nothing
	var amps = Array.fill(6, { |i| (\amp++(i+1)).asSymbol.kr(((0-i)>=0).asInteger)});
	var ratios = Array.fill(6, { |i| (\ratio++(i+1)).asSymbol.kr(1)});
	var fixed = Array.fill(6, {|i| (\fixed++(i+1)).asSymbol.kr(0)});
	var detunes = Array.fill(6, { |i| (\detune++(i+1)).asSymbol.kr(0)});
	// !!! TODO: add modwheel and LFO
	// lfo sensitivity
	var modsenses = Array.fill(6, { |i| (\modsens++(i+1)).asSymbol.kr(0)});
	// selects between FM = 0 and AM = 1
	var lfomode = \lfomode.kr(0);
	var lfofreq = \lfofreq.kr(1);
	var lfodepth = \lfodepth.kr(0.0);
	var lfo = SinOsc.ar(lfofreq, 0, lfodepth);
	var cutoff = \cutoff.kr(10000);
	var res = \rq.kr(1);

    var envs = Array.fill(6, { |i|
        var n = (i+1).asString;
        EnvGen.kr(Env.new(
            [
                0,
                (\atkl++n).asSymbol.kr(1.0),
                (\decl++n).asSymbol.kr(1.0),
                (\susl++n).asSymbol.kr(1.0),
                (\rell++n).asSymbol.kr(0.0),
            ], [
                (\atkt++n).asSymbol.kr(0.0),
                (\dect++n).asSymbol.kr(0.3),
                (\sust++n).asSymbol.kr(0.7),
                (\relt++n).asSymbol.kr(0.0),
            ],
            // releaseNode: 3,
            // loopNode: 1,
        ),
        gate: gate,
        timeScale: dur,
		// doneAction:2 ,
    )
    });

	var ctls = Array.fill(6, { |i|[
		Select.kr((fixed[i] > 0).asInteger, [freq * ratios[i], fixed[i]]) + detunes[i],
		// freq * ratios[i] + detunes[i],
		0,
		envs[i],
	]});

	var mods = Array.fill(6, { |i|
		Array.fill(6, { |n| (\mod++(i+1)++(n+1)).asSymbol.kr(0)});
	});

	var sig = FM7.ar(ctls, mods) * amps;
	// sig = sig * envs[0];
	sig = RLPF.ar(sig, cutoff, res);
	sig = sig * env;
	// sig = SinOsc.ar(freq) * EnvGen.kr(Env.perc(\atk.kr(0.01), \rel.kr(1)), gate, doneAction:2);
	Out.ar(out, Pan2.ar(Mix.ar(sig * amp), pan));
},
	variants: (
		alpha: [mod11: 1, mod12: 1, mod22: 1, ratio2: 0.25],
		organ: [ratio2: 1.01, ratio3: 1.02, ratio4: 2, ratio5: 3, ratio6: 0.5, amp2: 0.8, amp3: 0.6, amp4:0.5, amp5:0.4, amp6: 1]
	)
).add;
)

// (
// fmx7 basic example
// Pbindef(\a, \instrument, \fmx7,
//     \dur, 4,
//     \amp2, 1,
//     \ratio2, 1.1,
//     \mod13, 1,
//     \egrate31, 10.1,
//     \eglevel32, 0.1,
// ).play;
// )

// (
// fmx7 Pbindef random example
// Pbindef(\a, \instrument, \fmx7).play;
// Pbindef(\a, \amp, 1);
// 6.do { |i|
// 6.do { |n|
// Pbindef(\a, (\mod++(i+1)++(n+1)).asSymbol, 1.0.rand);
// };
// 4.do { |n|
// Pbindef(\a, (\egrate++(i+1)++(n+1)).asSymbol, rrand(0.0,0.25));
// Pbindef(\a, (\eglevel++(i+1)++(n+1)).asSymbol, 1.0.rand);
// };
// Pbindef(\a, (\amp++(i+1)).asSymbol, 1.0.rand);
// Pbindef(\a, (\freq++(i+1)).asSymbol, ([0.25,0.5]++(1..8)).wchoose((0.2!4)++(0.02!6)).postln);
// Pbindef(\a, \attack, rrand(0.01, 0.2));
// Pbindef(\a, \decay, rrand(0.01, 0.5));
// Pbindef(\a, \sustainLevel, rrand(0.3, 0.95));
// Pbindef(\a, \releaseTime, 1.0.rand);
// };
// )
