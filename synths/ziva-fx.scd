(
SynthDef(\delay, {
	var in = In.ar(\in.kr(0), 2);
	var out = \out.ir(0);
	var maxdelaytime = \maxdelaytime.ir(8);
	var delaytime = \delayt.kr(0.2);
	var feedback = \fb.kr(0.5);
	var wet = \wet.kr(0.5);
	var gate = \gate.kr(1);
    var env, local, sig, del;
    env = Linen.kr(gate, 0.05, 1, 0.1, 2);
	local = LocalIn.ar(2) + in;
	del = DelayN.ar(
		local,
		maxdelaytime: maxdelaytime,
		delaytime: delaytime,
		mul: feedback
	);
	LocalOut.ar(del);
    // XOut.ar(out, env, (local * (1-wet)) + (del * wet));
	sig = local;
    sig = sig * EnvGen.kr(Env(#[1, 1, 0], #[1, 1], -3, releaseNode: 1), gate, doneAction: Done.freeSelf);
    ReplaceOut.ar(out, sig)
}).add;

SynthDef(\distort, {|out =0 gate= 1|
	var source = In.ar(\in.kr(0),2);
	var env = Linen.kr(gate, 0.0, 1, 0.1, 2);
	var abs, excess,output;

	abs = source.abs;

	excess = (abs-0.1).max(0.0).min(0.9)/0.9;

	//original plus sinusoidal perturbation of amount based on absolute amplitude
	output = source+(excess*(sin(excess*2pi*5)*0.5-0.5));

	XOut.ar(out,env,output*env);

}).add;

SynthDef(\chorus, {
	var out = \out.kr(0);
	var in = \in.kr(0);
	var amp = \amp.kr(1);
	var gate = \gate.kr(1);
	var source = In.ar(in,2);
	var chorus;
	var env = Linen.kr(gate, 0.1, amp, 0.1, 2);

	chorus= Mix.fill(7, {

		var maxdelaytime= rrand(0.005,0.02);

		DelayC.ar(source, maxdelaytime,LFNoise1.kr(Rand(4.5,10.5),0.25*maxdelaytime,0.75*maxdelaytime) )

	});

	XOut.ar(out,env, chorus * env);

}).add;

SynthDef(\gverb, {
	var out = \out.kr(0);
	var amp = \amp.kr(0.2);
	var gate = \gate.kr(1);
	var room = \room.kr(10);
	var revtime = \revtime.kr(3);
	var damp = \damp.kr(0.5);
	// var in = In.ar(\in.kr(0));
	var in = In.ar(\in.kr(0), 2);
	var env;
	env = Linen.kr(gate, 0.05, 2, 0.2, 2);
	XOut.ar(out, env, GVerb.ar(Mix.ar(in), room, revtime, damp, mul:amp).softclip * env * 0.8);
}).add;
)