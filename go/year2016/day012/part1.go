package main

func part1() int {
	// v := 1
	// program, _ := parseInput(&v)
	program, _ := parseInput(nil)

	program.Run()

	return program.registers["a"]
}
