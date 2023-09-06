+ Nil {
	sound {|snd|
		var code = thisProcess.interpreter.cmdLine.split($ )[0];
		var connection = code.findRegexp("~[a-zA-Z0-9]+")[0][1].asSymbol;
		Pdef(connection, Pbind(\type, \dirt, \s, snd));
		Pdef(connection).play;
		^Pdef(connection);
	}

	n { |value| ^value.debug("n") }
}