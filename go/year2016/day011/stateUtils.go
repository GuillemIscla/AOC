package main

func (s State) NextStates(previousStates []State) []State {
	var result []State
	// We process for a next State with Elevator going up or down
	for _, ud := range []string{"up", "down"} {
		// We calculate such next floor
		var nextFloor int
		if ud == "up" {
			nextFloor = s.Elevator() + 1
		} else {
			nextFloor = s.Elevator() - 1
		}
		if nextFloor > 4 || nextFloor < 1 {
			continue
		}
		// We study each combination of one or two elements we can bring in the elevator
		combinations := Combinations12(s.itemsInFloor(s.Elevator()))
		for _, combination := range combinations {
			//We create a new state by applying this combination
			nextCargos := s.CargosCopy()
			for _, cInComb := range combination {
				for i := range nextCargos {
					if nextCargos[i] == cInComb {
						nextCargos[i].Floor = nextFloor
						break
					}
				}
			}
			nextState := NewState(nextFloor, nextCargos)

			//We check if is safe to end up in this nextState
			if nextState.IsSafe() {
				// We check if we ended up in that new state before (to trim cycles)
				isRepetead := false
				for _, ps := range append(result, previousStates...) {
					if ps.Equals(nextState) {
						isRepetead = true
						break
					}
				}
				if !isRepetead {
					//If every check passes, we append it to the result
					result = append(result, nextState)
				}
			}
		}

	}
	return result
}

func (s State) IsSafe() bool {
	// We make a check for each floor
	for floor := 1; floor <= 4; floor++ {
		itemsInFlor := s.itemsInFloor(floor)
		for _, itemInFloorI := range itemsInFlor {
			// In case we find a chip
			if !itemInFloorI.IsGenerator {
				// We look for any generator
				thereIsGenerator := false
				canBePowered := false
				for _, itemInFloorJ := range itemsInFlor {
					if itemInFloorJ.IsGenerator {
						// We testify there is a generator
						thereIsGenerator = true
						if itemInFloorI.Name == itemInFloorJ.Name {
							// If is a generator we can plug the chip into, this chip is fine
							canBePowered = true
							break
						}
					}

				}
				// If there is no a generator, the chip needs to be powered
				if thereIsGenerator && !canBePowered {
					return false
				}
			}
		}
	}
	return true
}

func (s State) IsFinal() bool {
	for _, c := range s.CargosCopy() {
		if c.Floor != 4 {
			return false
		}
	}
	return true
}

func (s State) Equals(other State) bool {
	return s.signature == other.signature
}

func Combinations12(items []Cargo) [][]Cargo {
	var result [][]Cargo

	for i := range items {
		result = append(result, []Cargo{items[i]})
		for j := i + 1; j < len(items); j++ {
			result = append(result, []Cargo{items[i], items[j]})
		}
	}

	return result
}

func (s State) itemsInFloor(floor int) []Cargo {
	var result []Cargo

	for _, c := range s.cargos {
		if c.Floor == floor {
			result = append(result, c)
		}
	}

	return result
}
