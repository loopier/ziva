+ Event {
	ziva {
		var compileString = "Tdef('ziva', {";
		compileString = compileString ++ "inf.do{ |i| ";
		this.keysValuesDo { |k,v|
			compileString = compileString ++ "\t%.();\n".format(k);
			compileString = compileString ++ "\t%.wait;\n".format(v);
		};
		compileString = compileString ++ "}})";
		compileString.interpret;
		^Tdef(\ziva);
	}

}
