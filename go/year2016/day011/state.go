package main

import (
	"fmt"
	"sort"
	"strings"
)

type State struct {
	elevator  int
	cargos    []Cargo
	signature string
}

func (s State) Elevator() int {
	return s.elevator
}

func (s State) CargosCopy() []Cargo {
	cargosCopy := make([]Cargo, len(s.cargos))
	copy(cargosCopy, s.cargos)
	return cargosCopy
}

func NewState(elevator int, cargos []Cargo) State {
	var chipsDistances [][2]int
	for _, elemI := range cargos {
		if !elemI.IsGenerator {
			for _, elemJ := range cargos {
				if elemJ.IsGenerator && elemJ.Name == elemI.Name {
					chipsDistances = append(chipsDistances, [2]int{elemI.Floor, elemJ.Floor})
				}
			}
		}
	}

	sort.Slice(chipsDistances, func(i, j int) bool {
		if chipsDistances[i][0] == chipsDistances[j][0] {
			return chipsDistances[i][1] < chipsDistances[j][1]
		}
		return chipsDistances[i][0] < chipsDistances[j][0]
	})

	return State{
		elevator:  elevator,
		cargos:    cargos,
		signature: fmt.Sprintf("%d-%v", elevator, chipsDistances),
	}
}

func (s State) String() string {
	output := s.signature + "\n"
	for floor := 4; floor > 0; floor-- {
		elevatorTube := "."
		if s.elevator == floor {
			elevatorTube = "E"
		}
		output = fmt.Sprintf("%vF%d %s  ", output, floor, elevatorTube)
		for column := range len(s.cargos) {
			var foundCargo = s.FindCargo(floor, column)
			if foundCargo != nil {
				output = fmt.Sprintf("%v%v ", output, foundCargo.ShortName)
			} else {
				output = fmt.Sprintf("%v.  ", output)
			}
		}
		output = output + "\n"
	}
	return output
}

func (s State) FindCargo(floor int, column int) *Cargo {
	var found *Cargo
	for _, c := range s.cargos {
		if c.Floor == floor && c.Column == column {
			found = &c
			return found
		}
	}
	return nil
}

type Cargo struct {
	Floor       int
	Column      int
	Name        string
	ShortName   string
	IsGenerator bool
}

func NewCargo(floor int, column int, name string, isGenerator bool) Cargo {

	shortName := strings.ToUpper(name[:1])
	if isGenerator {
		shortName += "G"
	} else {
		shortName += "M"
	}

	return Cargo{
		Floor:       floor,
		Column:      column,
		Name:        name,
		ShortName:   shortName,
		IsGenerator: isGenerator,
	}
}
