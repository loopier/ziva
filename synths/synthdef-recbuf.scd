(
SynthDef(\recbuf, { |monitor=1|
	var out = \out.kr(0);
	var amp = \amp.kr(1);
	var buf = \buf.kr(0);
	var rate = \rate.kr(1);
	var trig = \trig.kr(1);

	var in = SoundIn.ar(\in.kr(0));
	// var in = In.ar(\in.kr(0));
	var feedback = \feedback.kr(0);
	var rec = \rec.kr(1);
	var sig = in * amp * (rec > 0);

	RecordBuf.ar(sig, buf, recLevel: amp, preLevel: feedback, run: rec);
	Out.ar(out, sig!2 * monitor);
}).add;
)
