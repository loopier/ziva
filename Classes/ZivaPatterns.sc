Psynth {
	*new { |name ... pairs|
		^Pbind(\type, \ziva_synth, \instrument, name, *pairs);
	}
}

Psample {
	*new { |sound, ch=2 ... pairs|
		if(Ziva.samplesDict.includesKey(sound)) {
			^Pbind(\type, \sample, \sound, sound, \ch, ch, *pairs);
		} {
			"Sample '%' not found. Evaluate 'Ziva.samples' to see a list of available synths.".format(sound.asString).error;
		}
	}
}

// Prec {
// 	*new{ |name = \, length = 4, channels = 1|
// 		^P
// 	}
// }

Pmidi {
	*new { |midiout, ch=0|
		^Pbind(\type, \midi, \midiout, midiout, \chan, ch);
	}

	// cc { |num, val|
	// 	^Pchain(Pbind(\midi))
	// }
}

Pavldrums {
	*new { |midiout, ch=0|
		^Pbind(\type, \midi, \midiout, midiout, \chan, ch, \octave, 3, \amp, Pwhite(0.7));
	}
}