(
SynthDef(\schroeder_reverb, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var amp = \amp.kr(1);
	var drySig = \in.ar.asArray.sum;
	var wet = \wet.kr(1);
    var sig, fb;
	sig = SchroederReverb.ar(drySig!2, \mix.kr(0.3));
    Out.ar(out, sig * amp);
}).add;

SynthDef(\moorer_reverb, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var amp = \amp.kr(1);
	var drySig = \in.ar.asArray.sum;
	var wet = \wet.kr(1);
    var sig, fb;
	sig = SchroederReverb.ar(drySig!2, \mix.kr(0.3));
    Out.ar(out, sig * amp);
}).add;

SynthDef(\ziva_freeverb, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var amp = \amp.kr(1);
	var drySig = \in.ar.asArray.sum;
	var wet = \wet.kr(1);
    var sig, fb;
	var wetSig = FreeVerb.ar(drySig!2, \room.kr(0.3), \damp.kr(0.5));
	sig = (drySig * (1 - wet / 2)) + (wetSig * wet / 2);
    Out.ar(out, sig * amp);
}).add;

SynthDef(\ziva_dirtverb, {
    var out = \out.kr(0);
    var pan = \pan.kr(0);
    var amp = \amp.kr(1);
    var gate = \gate.kr(1);

	var drySig = \in.ar.asArray.sum;
	var wet = \wet.kr(1);

	var room = \room.kr(0);
	var size = \size.kr(0.1);
	var dry = \dry.kr(0);

	var numChannels = 2;

	var in, snd, loop, depth;

	// in = In.ar(dryBus, numChannels).asArray.sum;
	in = drySig;

	// in = in * room.lag(LFNoise1.kr(1).range(0.01, 0.02)); // regulate input

	4.do { in = AllpassN.ar(in, 0.03, { Rand(0.005, 0.02) }.dup(numChannels), 1) };

	depth = size.lag(0.02).linexp(0, 1, 0.01, 0.98); // change depth between 0.1 and 0.98
	loop = LocalIn.ar(numChannels) * { depth + Rand(0, 0.05) }.dup(numChannels);
	loop = OnePole.ar(loop, 0.5);  // 0-1

	loop = AllpassN.ar(loop, 0.05, { Rand(0.01, 0.05) }.dup(numChannels), 2);

	loop = DelayN.ar(loop, 0.3, [0.19, 0.26] + { Rand(-0.003, 0.003) }.dup(2));
	loop = AllpassN.ar(loop, 0.05, { Rand(0.03, 0.15) }.dup(numChannels), 2);

	loop = loop + in;
	loop = LeakDC.ar(loop);

	LocalOut.ar(loop);

	snd = loop;
	snd = ((drySig * (1 - wet / 2)) + (snd * wet / 2));//.lag(LFNoise1.kr(1).range(0.01, 0.02));
	// snd = snd * (1 - dry).lag(LFNoise1.kr(1).range(0.01, 0.02));

	snd = snd * EnvGen.kr(Env.asr, gate, doneAction:2);

	Out.ar(out, snd);
}).add;

)
