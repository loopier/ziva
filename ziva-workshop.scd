// =====================================================================
// Živa - live coding with SuperCollider patterns made easy (hopefully)
// =====================================================================


// =====================================================================
// SETUP
// =====================================================================
// start the server
Ziva.boot;
// you can set some sever options with arguments, e.g. to set more audio channels:
// Ziva.boot(inputChannels:2, outputChannels:2);
// or more memory
// Ziva.boot(numBuffers:16, memSize:32, maxNodes:32)

// load some samples.  This path is to a parent directory holding
// subdirectories, which themselves store the audio files.
// Each subdirectory name will become the sample name.
// Each sample will be accessed with the 'n' function.  More on this later.
Ziva.loadSamples("path/to/samples");
// You can add different sample lists, from different folders. They will all
// be stored and accessible. For example, you can load SuperDirt's
// samples alongside yours.
Ziva.loadSamples("~/.local/share/SuperCollider/downloaded-quarks/Dirt-Samples");
// To list all samples, evaluate this function
Ziva.sounds;

// To list all synths use !!!FIX: -- not working well
Ziva.synths;
// which will list all SynthDefs loaded on the server.  They can be loaded anytime as
// you would normally do with `SynthDef(...).add`


// =====================================================================
// PLAYING SOUNDS
// =====================================================================
// In SuperCollider (SC), there's a thing called `Pbind`.  It's a somewhat
// obscure name for a music score, a set of instructions to play music.
// It's syntax is very simple:
Pbind(\parametername1, VALUE, \parametername2, VALUE, ...);

// There are many parameters available, too many to list here (see Pbind's
// help if you want the details).  We're going to see some as we need them.
// The first one is
Pbind(\instrument, \acid);
// where `\acid` is the name of a SynthDef that should be loaded already if you
// booted the server with Ziva.boot

// This is just a score sheet that says, "score sheet to be played with \acid synth".
// As such, it does nothing by itself.
// We need it to give it to a musician
~lola = Pbind(\instrument, \acid);

// we can then tell the musician to play the score
~lola.play;

// but we cannot tell it to stop;
~lola.stop;
// WTF??? why didn't it stop?  One of SuperCollider's many complexities.
// We won't explain it here.
// For now, press CTRL+. (CTRL+DOT) to stop all sounds at any time (REMEMBER THIS
// YOU WILL NEED IT!!)

// In Živa, to simplify things, we'll put all our musicians in a band
(
[
	~lola,
].ziva;
)
// then we can tell the band to stop playing
Ziva.stop;
// or simply remove everyone from the band
(
[
	// ~lola,
	nil
].ziva;
)
// =====================================================================
// CHANGING THE SOUND
// =====================================================================
// the sound can be changed in many different ways. Some are common to
// samples and synths. Some are specific to samples, others to synths,
// and there are others that are specific to one type of synth.
// The functions can be chained.

// common parameters to samples and synths are:
// tempo
[~lola.fast].ziva; // try faster, fastest, slow, slower, slowest
[~lola.dur(1.3)].ziva; // for custom duration

// volume
[~lola.f].ziva; // try ff, fff, ffff, p, pp, ppp
[~lola.amp(0.2)].ziva; // BE CAREFUL!! NEVER GO ABOVE 1.0!!!

// legato (sustain)
[~lola.stacc].ziva; // try pizz, stass, stacc, tenuto, legato, pedal
[~lola.legato(1.2)].ziva, // custom length

// panning (distribution along stereo field)
[~lola.pan(-1)].ziva; // left
[~lola.fast.pan(1)].ziva; // right
[~lola.slow.pan(0)].ziva; // center -- default
[~lola.pingpong].ziva; // alternates between left and right

// euclidean rhythms
// creates a rhythm. It distributes as evenly as possible a number
// of hits (first argument) across a number fo beats (second argument).
[~lola.faster.bj(3,8)].ziva; // will create 3 hits over 8 beats
[~lola.faster.bj(5,8,1)].ziva;// will create 5 hits over 8 beats delayed
							// by 1 beat
[~lola.faster.bj(5,8,scramble:true)].ziva;// will create 5 random hits over
										// 8 beats
[~lola.faster.bj(3,8,sort:true)].ziva;// will create 5 silent beats then 3 hits
[~lola.faster.bj(3,8,reverse:true)].ziva;// reverse order from first example

// to create custom rhythms there's the `r` variables, which is a rest.
// it can be added to any pattern of any parameter (exept `dur`) in
// order to add rests
[~lola.deg([0,2,4,r].pseq)].ziva;
// a useful use for this is having an independent parameter.  It can be named
// anything. I usually use .r()
[~lola.deg((..7).prand).r([1,r,r,1,r,r,1,r,1,r,1,r].pseq)].ziva;

// SYNTHS
// when playing a synth, notes can be changed
[~lola.deg(2)].ziva;// plays the 3rd note from the scale
[~lola.deg([0,2,4])].ziva;// plays a major chord
[~lola.deg([0,2,4]).scale(Scale.minor)].ziva;// play minor chord
[~lola.oct(4)].ziva;// one octave lower (default is 5)

// synth parameters can also be modified
// to see what parameters are avaiable for a synth
Ziva.controls(\acid); // see the post window
[~lola.cutoff(8000)].ziva;
[~lola.cutoff(200)].ziva;

// SAMPLES
// to list the available samples
Ziva.sounds; // see the post window.
// The number between () is the number of samples with that name
~delia = Psample(\delia);
[~delia].ziva;
// change sample name
[~delia.sound(\tibetan)].ziva;
// change sample number (if greater than the number of samples in
// that sample folder it will wrap around)
[~delia.n(1)].ziva;
// rate
[~delia.rate(0.5)].ziva; // half speed
// sequence a list of rates. First argument is the number of steps in
// the sequence, the second is a list of rates to choose from.
[~delia.randrates(4, [1,-1,2,-1])].ziva;
// old cassette tape effect
[~delia.slow.tape(0.9)].ziva;
// starting point in the sample (0.0 for beginning, 1.0 for end)
[~delia.start(0.5)].ziva;
// slices the sample in a number of chunks (second argument), and plays
// a sequence of them. The size of the sequence is given by the
// first argument.
// This example sequences 4 pieces chosen from a list of 8 chunks.
[~delia.chop(4, 8)].ziva;


// =====================================================================
// SEQUENCING
// =====================================================================
// Parameters can be set to change automatically with PATTERNS.
// There is a HUGE amount of patterns in SuperCollider, but a lot can be
// done with very few. Here we'll present just a few.

// .pseq(repeats:inf) is used to repeat a list of values for a number of
// times. If not repeats are specified, it loops for ever.
[~lola.deg([0,2,4].pseq)].ziva;
// .prand(repeats:inf) pick a random value from the list on each event (hit)
[~lola.deg([0,1,2,3,4].prand)].ziva;
// patterns can be neted
[~lola.faster.deg([0,1,2, [7,8,9].pseq(2)].prand)].ziva;

// other interesting patterns are Place, Pshuf, Pbrown, Pwhite, Pstutter, ...
// See A-Practical-Guide/PG_02_Basic_Vocabulary in SCHelp.

// patterns can modulate any parameter
(
[
	~lola.faster.pizz.deg(Pbrown(0,7,1)).oct(6).cutoff(Pwhite(200,9000)).pan(Pwhite(-1.0)),
	~lola.faster.legato(Pwhite(0.1,1.2)).bj(4,7).cutoff(8000),
	~lola.oct([3,4].pseq),
].ziva;
)

// =====================================================================
// MODULATION
// =====================================================================
// Another way to modulate parameters is with LFOs (Low Frequency Oscillator).
// This example creates a sine wave oscillator and uses it to modulate the
// cutoff frequency of our \acid synth.
~sine1 = Ziva.lfo(1, wave:\sine, freq:0.6, min:200, max:8000  );
[~lola.legato.cutoff(~sine1)].ziva;
// you can create as many LFOs as you want, but the first parameter must be
// different for each LFO or it will be overwitten (can be a number or a symbol)
// other LFO wave shapes are: \sine, \saw, \pulse, \tri, \noise0, \noise1, \noise2

// =====================================================================
// EFFECTS
// =====================================================================
// Effects are applied to tracks, not sounds.
// There are 4 tracks available.
// Any number of effects can be applied to each track (including none).
// Order matters.
// To list the available effects
Ziva.fx; // see the post window

Ziva.boot; // this must be called -- it's of bug, will be fixed
// to setup effects for tracks
Ziva.track(0, \lowpass, \reverbL);
Ziva.track(1, \delay);
// then connect each sond to the desired track
(
[
	~lola.oct([3,4]).cutoff(9000) >> 0,
	~lola.pizz.slow >> 1,
].ziva;
)
Ziva.track(0); // remove the fx from track 0
