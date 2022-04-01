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
    var env = EnvGen.ar(
        Env.adsr(atk, dec, sus, rel),
        gate: gate,
        doneAction: 2
	);
    var sig, fb;
    var vel = \vel.kr(0.8) * 100;
	var decay = 0.8;
	var release = 0.8;
	var hard = 0.4;
	var velhard = 0.8;
	var muffle = 0.8;
	var velmuff = 0.8;
	var velcurve = 0.8;
	var stereo = 0.2;
	var tune = 0.5;
	var random = 0.1;
	var stretch = 0.1;
	var sustain = 0;
	var mul = 1;
	var add = 0;
    sig = MdaPiano.ar(freq, gate, vel, decay, release, hard, velhard, muffle, velmuff, velcurve, stereo, tune, random, stretch, sustain, mul, add);
    sig = sig * EnvGen.kr(Env.asr(0, 1, 0.1), gate, doneAction: 2);
    Out.ar(out, Pan2.ar(sig * env * amp, pan));
}).add;
)