use super::aunt_sue_notes::AuntSueNotes;

pub struct AuntSueTickerTape {
    pub children: usize,
    pub cats: usize,
    pub samoyeds: usize,
    pub pomeranians: usize,
    pub akitas: usize,
    pub vizslas: usize,
    pub goldfish: usize,
    pub trees: usize,
    pub cars: usize,
    pub perfumes: usize,
}

impl AuntSueTickerTape {
    pub fn match_with_notes(&self, aunt_sue_notes: &AuntSueNotes) -> bool {
        aunt_sue_notes
            .children
            .map(|children| self.children == children)
            .unwrap_or(true)
            && aunt_sue_notes
                .cats
                .map(|cats| self.cats == cats)
                .unwrap_or(true)
            && aunt_sue_notes
                .samoyeds
                .map(|samoyeds| self.samoyeds == samoyeds)
                .unwrap_or(true)
            && aunt_sue_notes
                .pomeranians
                .map(|pomeranians| self.pomeranians == pomeranians)
                .unwrap_or(true)
            && aunt_sue_notes
                .akitas
                .map(|akitas| self.akitas == akitas)
                .unwrap_or(true)
            && aunt_sue_notes
                .vizslas
                .map(|vizslas| self.vizslas == vizslas)
                .unwrap_or(true)
            && aunt_sue_notes
                .goldfish
                .map(|goldfish| self.goldfish == goldfish)
                .unwrap_or(true)
            && aunt_sue_notes
                .trees
                .map(|trees| self.trees == trees)
                .unwrap_or(true)
            && aunt_sue_notes
                .cars
                .map(|cars| self.cars == cars)
                .unwrap_or(true)
            && aunt_sue_notes
                .perfumes
                .map(|perfumes| self.perfumes == perfumes)
                .unwrap_or(true)
    }

    pub fn match_with_notes_modified(&self, aunt_sue_notes: &AuntSueNotes) -> bool {
        aunt_sue_notes
            .children
            .map(|children| self.children == children)
            .unwrap_or(true)
            && aunt_sue_notes
                .cats
                .map(|cats| self.cats < cats)
                .unwrap_or(true)
            && aunt_sue_notes
                .samoyeds
                .map(|samoyeds| self.samoyeds == samoyeds)
                .unwrap_or(true)
            && aunt_sue_notes
                .pomeranians
                .map(|pomeranians| self.pomeranians > pomeranians)
                .unwrap_or(true)
            && aunt_sue_notes
                .akitas
                .map(|akitas| self.akitas == akitas)
                .unwrap_or(true)
            && aunt_sue_notes
                .vizslas
                .map(|vizslas| self.vizslas == vizslas)
                .unwrap_or(true)
            && aunt_sue_notes
                .goldfish
                .map(|goldfish| self.goldfish > goldfish)
                .unwrap_or(true)
            && aunt_sue_notes
                .trees
                .map(|trees| self.trees < trees)
                .unwrap_or(true)
            && aunt_sue_notes
                .cars
                .map(|cars| self.cars == cars)
                .unwrap_or(true)
            && aunt_sue_notes
                .perfumes
                .map(|perfumes| self.perfumes == perfumes)
                .unwrap_or(true)
    }
}
