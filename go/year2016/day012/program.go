package main

import (
	"fmt"
	"strconv"
)

type Instruction interface {
	Process(registers map[string]int, pointer *int)
	GetRegisters() []string
}

type ValueType int

const (
	ValueInt ValueType = iota
	ValueStr
)

type Slot struct {
	kind     ValueType
	intValue int
	strValue string
}

func NewSlot(raw string) Slot {
	parsed, error := strconv.Atoi(raw)
	if error == nil {
		return Slot{
			kind:     ValueInt,
			intValue: parsed,
		}
	} else {
		return Slot{
			kind:     ValueStr,
			strValue: raw,
		}
	}
}

func (s Slot) GetRegisters() []string {
	switch s.kind {
	case ValueInt:
		return []string{}
	case ValueStr:
		return []string{s.strValue}
	}
	return []string{}
}

func (s Slot) GetIntValue(registers map[string]int) int {
	switch s.kind {
	case ValueInt:
		return s.intValue
	case ValueStr:
		return registers[s.strValue]
	}
	return 0
}

type cpy struct {
	one Slot
	two string
}

func (s cpy) Process(registers map[string]int, pointer *int) {
	registers[s.two] = s.one.GetIntValue(registers)
	*pointer += 1
}

func (s cpy) GetRegisters() []string {
	var registers []string
	registers = append(registers, s.one.GetRegisters()...)
	registers = append(registers, s.two)
	return registers
}

type inc struct {
	one string
}

func (s inc) Process(registers map[string]int, pointer *int) {
	registers[s.one] += 1
	*pointer += 1
}

func (s inc) GetRegisters() []string {
	return []string{}
}

type dec struct {
	one string
}

func (s dec) GetRegisters() []string {
	return []string{}
}

func (s dec) Process(registers map[string]int, pointer *int) {
	registers[s.one] -= 1
	*pointer += 1
}

type jnz struct {
	one Slot
	two Slot
}

func (s jnz) Process(registers map[string]int, pointer *int) {
	if s.one.GetIntValue(registers) != 0 {
		*pointer += s.two.GetIntValue(registers)
	} else {
		*pointer += 1
	}
}

func (s jnz) GetRegisters() []string {
	var registers []string
	registers = append(registers, s.one.GetRegisters()...)
	registers = append(registers, s.two.GetRegisters()...)
	return registers
}

type Program struct {
	pointer      int
	instructions []Instruction
	registers    map[string]int
}

func NewProgram(instructions []Instruction) Program {
	registers := make(map[string]int)

	for _, instruction := range instructions {
		newRegisters := instruction.GetRegisters()
		for _, newResgister := range newRegisters {
			_, ok := registers[newResgister]
			if !ok {
				registers[newResgister] = 0
			}
		}
	}

	return Program{
		pointer:      0,
		instructions: instructions,
		registers:    registers,
	}
}

func (p Program) String() string {
	return fmt.Sprintf("%#v, %v \n", p.instructions[p.pointer], p.registers)
}

func (p Program) Run() {
	for p.pointer >= 0 && p.pointer < len(p.instructions) {
		p.instructions[p.pointer].Process(p.registers, &p.pointer)
	}
}
