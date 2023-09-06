+ Pdef {
	doesNotUnderstand { |selector, args|
		var pairs;
		if(this.source.isNil) {^nil};
		pairs = this.source.patternpairs;
		args.debug("values");
		pairs = pairs.asDict;
		pairs[selector] = args;
		pairs = pairs.asPairs;
		pairs.debug("pairs");
		this.source = Pbind(*pairs);
	}
}