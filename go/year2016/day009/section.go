package main

import (
	"fmt"
	"regexp"
	"strconv"
)

type Section interface {
	Length() int
}

type Marker struct {
	MarkedLength int
	MarkedCount  int
}

func (m Marker) String() string {
	return fmt.Sprintf("(%dx%d)", m.MarkedLength, m.MarkedCount)
}

type Text struct {
	Size int
}

func (m Text) String() string {
	return fmt.Sprintf("[%d]", m.Size)
}

func (m Marker) Divide(sections []Section) ([]Section, []Section) {
	var sectionsToProcess []Section
	var sectionsNotToProcess []Section
	var accumulatedLength int
	for i := 0; i < len(sections); i++ {
		accumulatedLength += sections[i].Length()
		if accumulatedLength == m.MarkedLength {
			sectionsToProcess = sections[:i+1]
			sectionsNotToProcess = sections[i+1:]
			break
		}
		if accumulatedLength > m.MarkedLength {
			if sections[i].Length() > 0 {
				if t, ok := sections[i].(Text); ok {
					sectionsToProcess = sections[:i]
					sectionsToProcess = append(sectionsToProcess, Text{Size: m.MarkedLength + t.Size - accumulatedLength})
					sectionsNotToProcess = sections[i+1:]
					sectionsNotToProcess = append([]Section{Text{Size: accumulatedLength - m.MarkedLength}}, sectionsNotToProcess...)
					break
				} else {
					panic(fmt.Sprintf("Unexpected section marker: %T", sections[i]))
				}
			}
		}
	}
	return sectionsToProcess, sectionsNotToProcess
}

func (m Marker) Length() int {
	return len(fmt.Sprint(m.MarkedLength)) + len(fmt.Sprint(m.MarkedCount)) + 3
}

func NewMarker(raw string) Marker {
	re := regexp.MustCompile(`([0-9]+)x([0-9]+)`)
	matches := re.FindStringSubmatch(raw)
	markedLength, _ := strconv.Atoi(matches[1])
	markedCount, _ := strconv.Atoi(matches[2])

	return Marker{
		MarkedLength: markedLength,
		MarkedCount:  markedCount,
	}
}

func (t Text) Length() int {
	return t.Size
}
