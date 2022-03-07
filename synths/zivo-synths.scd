(
SynthDef(\playbufm, { |out=0, amp=0.5, pan=0, buf, rate=1, t_trig=1, start=0, attack=0.01, sustain=1, release=0.01, doneAction=2, tape=0|
	var sig, end, startpos, isForward, frames;
	isForward = (rate < 0);
	rate = rate * Select.kr(tape.ceil, [1, LFNoise2.ar(LFNoise2.ar(9).range(0.5,9)).range(0.96 - tape,1.04 + tape)]);
	frames = BufFrames.kr(buf);
	startpos = abs( (frames * isForward) - (frames * start) ) - (2 * isForward);
	sig = PlayBuf.ar(1, buf, BufRateScale.kr(buf) * rate, 1, startpos);
	sig = sig * EnvGen.kr(Env.linen(attack, sustain, release), t_trig, doneAction:doneAction);
	sig = Mix.ar(sig) * amp;
	// sig = Pan2.ar(sig, pan);
	Out.ar(out, Pan2.ar(sig,pan));
}).add;

SynthDef(\playbuf, { |out=0, amp=0.5, pan=0, buf, rate=1, gate=1, start=0, attack=0.01, decayTime=0.3, sustainLevel=0.5, releaseTime=0.01, tape=0|
	var sig, end, startpos, isForward, frames;
	isForward = (rate < 0);
	rate = rate * Select.kr(tape.ceil, [1, LFNoise2.ar(LFNoise2.ar(9).range(0.5,9)).range(0.96 - tape,1.04 + tape)]);
	frames = BufFrames.kr(buf);
	startpos = abs( (frames * isForward) - (frames * start) ) - (2 * isForward);
	sig = PlayBuf.ar(2, buf, BufRateScale.kr(buf) * rate, 1, startpos);
	// sig = sig * EnvGen.kr(Env.linen(attack, sustain, release), t_trig, doneAction:doneAction);
	sig = sig * Env.adsr(attack, decayTime, sustainLevel, releaseTime).kr(2, gate);
	// sig = Mix.ar(sig) * amp;
	sig = sig * amp;
	// sig = Pan2.ar(sig, pan);
	sig = Splay.ar(sig, 0.2, center:pan);
	Out.ar(out, sig);
}).add;

SynthDef(\playbufflat, { |out=0, amp=0.5, pan=0, buf, rate=1, gate=1, start=0, attack=0.01, decayTime=0.3, sustainLevel=0.5, releaseTime=0.01|
	var sig, end, startpos, isForward, frames;
	isForward = (rate < 0);
	frames = BufFrames.kr(buf);
	startpos = abs( (frames * isForward) - (frames * start) ) - (2 * isForward);
	sig = PlayBuf.ar(2, buf, BufRateScale.kr(buf) * rate, 1, startpos);
	// sig = sig * EnvGen.kr(Env.linen(attack, sustain, release), t_trig, doneAction:doneAction);
	sig = sig * Env.adsr(attack, decayTime, sustainLevel, releaseTime).kr(2, gate);
	sig = Mix.ar(sig) * amp;
	sig = sig * amp;
	// sig = Pan2.ar(sig, pan);
	Out.ar(out, sig);
}).add;
)