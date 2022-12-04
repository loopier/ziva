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
// be stored and accessible. For example, you can load the samples that come with
// this quark.
Ziva.loadSamples("~/.local/share/SuperCollider/downloaded-quarks/ziva/samples");
// To list all samples, evaluate this function
Ziva.sampls;

// To list all synths use
Ziva.synths;
// which will list all SynthDefs loaded on the server.  New synths can be loaded
// anytime as you would normally do with `SynthDef(...).add`

// To list all samples and synths use
Ziva.sounds;

// =====================================================================
// PLAYING SOUNDS
// =====================================================================
// To play a sound we need an instrument, and a score sheet.
// In Živa there are scores for three types of instryments: synths,
// samples and MIDI.
// For each instrument we need a specific type of score sheet, being
// either one fo these:
Psynth()
Psample()
Pmidi()

// When we create a score sheet, we need to specify the instrument name.
// There are many synths, samples and MIDI channels that can play any given
// score sheet.
// The specific instrument is added to the score with a `symbol`, a name
// with a leading `\`. For example: `\acid`.
// In this case, `\acid` is the name of a SynthDef that should be
// already loaded if you booted the server with Ziva.boot.
Psynth(\synthName) // e.g.: Psynth(\acid)
Psample(\acid) // e.g.: Psample(\delia)

// Pmidi is a special case and we'll skip it for now.

// The previous is just a score sheet that says, "score sheet to be played
// with \acid synth".
// As such, it does nothing by itself; we need it to give it to a musician.
~lola = Psynth(\acid);

// `~lola` is variable name. It can be anything, as long as it's a
// combination of any number of alphanumeric characters, starting with
// `~` followed by a lower-case letter.

// now we could tell the musician to play the score
~lola.play;

// but we cannot tell it to stop;
~lola.stop;
// WTF??? why didn't it stop?  One of SuperCollider's many complexities.
// We won't explain it here.
// For now, press CTRL+. (CTRL+DOT) to stop all sounds at any time (REMEMBER THIS
// YOU WILL NEED IT!!)

// In Živa, to can put all our musicians in a band
(
[
	~lola,
].ziva;
)
// then we can tell the band to stop playing with
Ziva.stop;
// or simply remove everyone from the band
(
[
	nil
].ziva;
)
// =====================================================================
// CHANGING THE SOUND
// =====================================================================
// the sound can be changed in many different ways. Some are common to
// samples, synths and MIDI. Some are specific to samples, others to synths,
// and there are others that are specific to one type of synth.
// The functions can be chained.

// In the following examples I'm using one-liners because I'm showing
// one parameter at a time and to keep file short, but while live coding
// I find it more convenient to use the multi-line syntax shown above:
(
[
	~lola,
].ziva;
)

// common parameters to samples and synths are:
// tempo
[~lola.fast].ziva; // try faster, fastest, slow, slower, slowest
[~lola.dur(1.3)].ziva; // for custom duration

// volume
[~lola.f].ziva; // try ff, fff, ffff, p, pp, ppp
[~lola.amp(0.2)].ziva; // BE CAREFUL!! NEVER GO ABOVE 1.0!!!

// legato (sustain)
[~lola.stacc].ziva; // try pizz, stass, stacc, tenuto, legato, pedal
[~lola.leg(1.2)].ziva, // custom length

// panning (distribution along stereo field)
[~lola.left].ziva;
[~lola.right].ziva;
[~lola.pan(-1)].ziva; // left
[~lola.pan(-0.2)].ziva; // slightly to the left
[~lola.pan(1)].ziva; // right
[~lola.pan(0)].ziva; // center -- default
[~lola.pingpong].ziva; // alternates between left and right
[~lola.randpan].ziva; // sets a different position on every note

// SYNTHS
// There are different ways to set the note of a synth:
// midinote, note, degree and frequency.

// We can set the MIDI note from 0 to 127
[~lola.midinote(60)].ziva; // plays the middle C

// `deg` or `degree` sets the note in the current scale, and
// in the current octave, 0 being the root.
// Notes played with `deg` will always be in tune.
// This is the method I normally use.
[~lola.deg(0)].ziva;// plays the root in the current octave
[~lola.deg(2)].ziva;// plays the 3rd note from the scale
[~lola.deg([0,2,4])].ziva;// plays a major chord
[~lola.deg([0,2,4]).scale(\minor)].ziva;// plays minor chord
[~lola.oct(4)].ziva;// one octave lower (default is 5)
// degree notes can be alterated with sharps (s) and flats (b)
[~lola.deg([0,2b,4])].ziva; // plays a minor chord although the scale is major
[~lola.deg([0,2s,4]).scale(\minor)].ziva; // plays a major chord although the scale is minor

// to list all available scale names in the post window
Scale.directory;

// `note` sets the cromatic note from the root at the current octave
[~lola.note(0)].ziva;// plays the root note in the current octave
[~lola.note(1)].ziva;// plays half step above the root note in the current octave
[~lola.note(-2)].ziva;// plays whole step below the root note in the current octave

// To play a frequency in herz we use `freq`
[~lola.freq(100)].ziva;

// synth parameters can also be modified
// to see what parameters are avaiable for a synth
Ziva.controls(\acid); // see the post window
[~lola.cutoff(8000)].ziva;
[~lola.cutoff(200)].ziva;

// SAMPLES
// to list the available samples
Ziva.samples; // see the post window.
// The number displayed between () is the number of samples with that name

// Let's create a musician that plays a sample
~delia = Psample(\delia);
[~delia].ziva;
// change sample -- this is not permament, as soon as you remove the `sound()`
// method it will get back to the original.
[~delia.sound(\catchot)].ziva;
// change sample number (if greater than the number of samples in
// that sample folder it will wrapped around)
[~delia.n(1)].ziva;
// change the playing speed
[~delia.speed(0.5)].ziva; // half speed
// sequence a list of speeds. First argument is the number of elements in
// the sequence, the second is a list of speeds to choose from.
// It will be different on every evaluation.
[~delia.randspeeds(4, [1,-1,2,-1])].ziva;
// old cassette tape effect
[~delia.slow.tape(0.9)].ziva;
// change the starting point in the sample (0.0 for beginning, 1.0 for end)
[~delia.start(0.5)].ziva;
// slices the sample in a number of chunks (second argument), and plays
// a sequence of them. The size of the sequence is given by the
// first argument.
// This example sequences 4 pieces chosen from a list of 8 chunks.
// It will be different every time it's evaluated.
[~delia.chop(4, 8)].ziva;

// MIDI
// MIDI instruments can also be played, but MIDI needs to be initialized first.
// Initialize the client
MIDIClient.init;
// connect the first SuperCollider output 'MIDIOut(0)' to your MIDI device in
// your User Iterface.
// This is different for each operative system.
// Then create a musician that plays that instrument.
~wendy = Pmidi(MIDIOut(0));
// 'MIDIOut(0)' syntax may be different for OSX and Windows -- this is just Linux
// MIDI channel is optional, default is 0 -- in SC MIDI channels range 0-15
[ ~wendy.oct(4).deg([0,2,4].pseq) ].ziva;

// =====================================================================
// SEQUENCING
// =====================================================================
// Parameters can be set to change automatically with PATTERNS.
// There is a HUGE amount of patterns in SuperCollider, but a lot can be
// done with very few. Here we'll present only some of them.

// .pseq(repeats:inf) is used to repeat a list of values for a number of
// times. If no 'repeats' are specified, it loops for ever.
[~lola.fast.deg([0,2,4].pseq)].ziva;
// play the sequence twice, then stop
[~lola.fast.deg([0,2,4].pseq(2))].ziva;
// .prand(repeats:inf) picks a random value from the list on each event (hit)
[~lola.fast.deg([0,1,2,3,4].prand)].ziva;
// play 2 notes then stop.
[~lola.fast.deg([0,1,2,3,4].prand(2))].ziva;
// patterns can be nested
[~lola.faster.deg([0,1,2, [7,8,9].pseq(2)].pseq)].ziva;

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
// RHYTHMS
// =====================================================================
// Euclidean Rhythms
// It distributes as evenly as possible a number of hits (first argument)
// across a number fo beats (second argument).
[~lola.faster.bj(3,8)].ziva; // will create 3 hits over 8 beats
[~lola.faster.bj(5,8,1)].ziva;	// will create 5 hits over 8 beats delayed
								// by 1 beat
[~lola.faster.bj(5,8,scramble:true)].ziva;	// will create 5 random hits over
											// 8 beats but with a random distribution
[~lola.faster.bj(3,8,sort:true)].ziva;// will create 5 silent beats then 3 hits
[~lola.faster.bj(3,8,reverse:true)].ziva;// reverse order from first example

// To create custom rhythms there's the `r` variable, which is a rest.
// It can be added to any pattern of any parameter in order to add rests.
// More on patterns later.
[~lola.deg([0,r,4,r].pseq)].ziva;
// It is useful having an independent parameter for rhythms.  It can be named
// anything. I usually use .r()
[~lola.deg((..7).prand).r([1,r,r,1,r,r,1,r,1,r,1,r].pseq)].ziva;

// There are also some default rhtyhms.
// To list them
Ziva.rhythms;
// To see a rhythm pattern put the name symbol as an argument to Ziva.rhythm(\rhythmName)
Ziva.rhythm(\clave);
// Rhtyhms can be applied to lists of values.
// When a rhythm is invoked on a list, the elements will
// replace the hits (1s) in the pattern keeping the rests.
// to see how it works
[0,2,4].clave; // see the post window
// audio example
[~lola.faster.deg([0,4].clave.pseq)].ziva;
// this can by applied to any sound argument

// some rhythms are divided into two bars that can be played in reversed order
// e.g.: clave can be 2/3
Ziva.rhythm(\clave); // this shows two nested arrays, each one being a bar
[0,2,4].clave;
// or 3/2
[0,2,4].clave(reverse:1);

// =====================================================================
// MODULATION
// =====================================================================
// Another way to modulate parameters is with LFOs (Low Frequency Oscillator).
// This example creates a sine wave oscillator and uses it to modulate the
// cutoff frequency of our \acid synth.
~sine1 = Ziva.lfo( 1, wave:\sine, freq:0.6, min:200, max:8000  );
[~lola.legato.cutoff(~sine1)].ziva;
// you can create as many LFOs as you want, but the first parameter must be
// different for each LFO or it will be overwitten (can be a number or a symbol)
// other LFO wave shapes are: \sine, \saw, \pulse, \tri, \noise0, \noise1, \noise2
// Parameters, when put in order, don't need the parameter name, so the above
// example can be written like
~sine1 = Ziva.lfo(1, \sine, 0.6, 200, 8000);

// =====================================================================
// EFFECTS
// =====================================================================
// Effects are applied to tracks, not sounds.
// Any number of effects can be applied to each track (including none).
// Order matters.
// To list the available effect names
Ziva.fx; // see the post window

// Let's create a track #0 with a lowpass filter and a large reverb.
Ziva.track(0, \lpf, \reverbL);
// and a second track (#1) with just a delay.
Ziva.track(1, \delay);
// then connect each sond to the desired track
(
[
	~lola.oct([3,4]).cutoff(9000) >> 0,
	~lola.pizz.slow >> 1,
].ziva;
)
Ziva.track(0); // remove the fx from track 0
Ziva.track(1); // remove the fx from track 1
// try re-evaluating the track with new effects
Ziva.track(1, \cyberpunk);