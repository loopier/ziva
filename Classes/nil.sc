+ Nil {
	sound {|snd|
		var code = thisProcess.interpreter.cmdLine.split($ )[0];
		// SC variable name is a 'connection' in SuperDirt (d1, d2, ...)
		var connection = code.findRegexp("~[a-zA-Z0-9]+")[0][1].replace("~", "").asSymbol;
		Pdef(connection, Pbind(\type, \dirt, \s, snd, \n, 0));
		Pdef(connection).play;
		History.eval("% = Pdef('%')".format(code, connection));
		History.eval("%.play".format(code));
		^Pdef(connection);
	}

	s { |snd| ^this.sound(snd) }
}