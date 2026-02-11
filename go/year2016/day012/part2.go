package main

func part2() int {
	// v := 1
	// program, _ := parseInput(&v)
	program, _ := parseInput(nil)

	program.registers["c"] = 1

	program.Run()

	return program.registers["a"]
}
