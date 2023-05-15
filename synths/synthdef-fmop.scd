(
SynthDef(\fmop, {
	var out = \out.kr(0);
	var pan = \pan.kr(0);
	var amp = \amp.kr(1) * (-12.dbamp);
	var freq = \freq.kr(440);
	var ratio = \ratio.kr(1);
	var mod = \in.ar(0);
	var modamt = \modamt.ar(1);
	var gate = \gate.kr(1);
	var atk = \atk.kr(0.01);
	var dec = \dec.kr(0.3);
	var sus = \sus.kr(1.0);
	var rel = \rel.kr(0.0);
	var atkl = \atkl.kr(1.0);
	var decl = \decl.kr(1.0);
	// var susl = \susl.kr(1);
	var rell = \rell.kr(0.0);
	// var atkt = \atkt.kr(0.01);
	// var dect = \dect.kr(1);
	var sust = \sust.kr(1.0);
	// var relt = \relt.kr(0);
	// var env = EnvGen.kr(Env.adsr(atk, dec, sus, rel), gate, doneAction:2);
	var ads = [dec, sust].normalizeSum * \sustain.kr(1.0);
	var env = EnvGen.kr(Env.new([0, atkl, decl, sus, rell],[atk, ads[0], ads[1], rel], \lin), gate, doneAction:2);
	var sig = SinOsc.ar(freq * ratio, mod * modamt, amp);
	Out.ar(out, Pan2.ar(sig * env, pan));
}).add;
)
