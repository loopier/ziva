// from SynthDEFaults:
// - A Collection of Traditional SynthDefs -
// under GNU GPL 3 as per SuperCollider license
// Organized by Zé Craum
// Examples at: http://sccode.org/1-5aE
(
SynthDef(\piano, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var freq = \freq.kr(440);
    var amp = \amp.kr(1);// * (-10.dbamp);
    var atk = \atk.kr(0.01);
    var dec = \dec.kr(0.3);
    var sus = \sus.kr(0.5);
    var rel = \rel.kr(1.0);
    var gate = \gate.kr(1);
	var t60 = \t60.kr(1);
	var damp = \damp.kr(0);
	var low = \low.kr(1);
	var mid = \mid.kr(1);
	var high = \high.kr(1);
	var lowcut = \lowcut.kr(500);
	var highcut = \highcut.kr(2000);
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel),
        gate: gate,
        doneAction: 2
	);
    var sig, fb;
    var vel = \vel.kr(0.8) * 100;
	var decay = \decay.kr(0.8);
	var release = \release.kr(0.8);
	var hard = \hard.kr(0.4);
	var velhard = \velhard.kr(0.8);
	var muffle = \muffle.kr(0.8);
	var velmuff = \velmuff.kr(0.8);
	var velcurve = \velcurve.kr(0.8);
	var stereo = \stereo.kr(1);
	var tune = \tune.kr(0.5);
	var random = \random.kr(0.1);
	var stretch = \stretch.kr(0.1);
	var pedal = \pedal.kr(0);
	var mul = 1;
	var add = 0;
    sig = MdaPiano.ar(freq, gate, vel, decay, release, hard, velhard, muffle, velmuff, velcurve, stereo, tune, random, stretch, pedal, mul, add);
    sig = sig * EnvGen.kr(Env.asr(0, 1, 0.1), gate, doneAction: 2);
	sig = JPverb.ar(sig, t60: t60, damp: damp, low: low, mid: mid, high: high, lowcut: lowcut, highcut: highcut);
    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).add;
)