// + Pdef {

// 	doesNotUnderstand { |selector, args|
// 		var pairs;
// 		pairs = this.patternpairs.asDict.put(selector.asSymbol, args).asPairs;
// 		this.source = Pbind(*pairs);
// 	}
// }