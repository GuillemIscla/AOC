package main

func part2() int {
	state, _ := parseInput(nil)

	cargosPlus := append(state.CargosCopy(),
		Cargo{
			Floor:       1,
			Column:      10,
			Name:        "elerium",
			ShortName:   "EG",
			IsGenerator: true,
		},
		Cargo{
			Floor:       1,
			Column:      11,
			Name:        "elerium",
			ShortName:   "EM",
			IsGenerator: false,
		},
		Cargo{
			Floor:       1,
			Column:      12,
			Name:        "dilithium",
			ShortName:   "DG",
			IsGenerator: true,
		},
		Cargo{
			Floor:       1,
			Column:      13,
			Name:        "dilithium",
			ShortName:   "DM",
			IsGenerator: false,
		},
	)

	state = NewState(state.Elevator(), cargosPlus)

	node, _ := MinSteps(state)

	return len(node.Path) - 1
}
