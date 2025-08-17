package main

import "fmt"

func part2() int {
	compressed, _ := parseInput(nil)

	uncompressed := uncompress(normalise(compressed))

	return uncompressed.Size
}

func uncompress(sections []Section) Text {
	var output Text
	var inMarker []Section
	outsideMarker := sections
	for {
		if len(outsideMarker) == 0 {
			break
		}
		section := outsideMarker[0]
		outsideMarker = outsideMarker[1:]
		if m, ok := section.(Marker); ok {
			inMarker, outsideMarker = m.Divide(outsideMarker)
			output = Text{Size: output.Size + uncompress(inMarker).Size*m.MarkedCount}
		} else if t, ok := section.(Text); ok {
			output = Text{Size: output.Size + t.Size}
		} else {
			panic(fmt.Sprintf("Unexpected section type: %T", section))
		}
	}
	return output
}

func normalise(input string) []Section {
	var output []Section
	var accumulatedText string
	var inMarker bool
	var count int
	for i := 0; i < len(input); i++ {
		if inMarker {
			accumulatedText += string(input[i])
			if input[i] == ')' {
				inMarker = false
				output = append(output, NewMarker(accumulatedText))
				accumulatedText = ""
				count = 0
			}
		} else {
			if input[i] == '(' {
				inMarker = true
				if count > 0 {
					output = append(output, Text{Size: count})
				}
				accumulatedText = "("
			}
			count++
			if i == len(input)-1 {
				output = append(output, Text{Size: count})
			}
		}
	}
	return output
}

func IsThereAMarker(sections []Section) bool {
	for i := 0; i < len(sections); i++ {
		if _, ok := sections[i].(Marker); ok {
			return true
		}
	}
	return false
}

func CalculateLength(sections []Section) int {
	totalLength := 0
	for i := 0; i < len(sections); i++ {
		if t, ok := sections[i].(Text); ok {
			totalLength += t.Length()
		}
	}
	return totalLength
}
