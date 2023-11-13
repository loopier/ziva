+ Nil {
	sound {|snd|
		var code = thisProcess.interpreter.cmdLine.split($ )[0];
		var name = code.findRegexp("~[a-zA-Z0-9]+")[0][1].replace("~", "").asSymbol;
		Ziva.newPlayer(code.asSymbol, snd).debug("New instrument");
		History.eval("% = Ndef('%')".format(code, name));
		// ^Ndef(name);
	}

	s { |snd| this.sound(snd) }

	fx {|effects|
		var code = thisProcess.interpreter.cmdLine.split($ )[0];
		var name = code.findRegexp("~[a-zA-Z0-9]+")[0][1].replace("~", "").asSymbol;
		History.eval("% = Ndef('%', { 'in'.ar(0!2) })".format(code, name));
		^Ndef(name);
	}
}