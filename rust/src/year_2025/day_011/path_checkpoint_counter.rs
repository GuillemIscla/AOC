use std::iter::Sum;

#[derive(Default, Clone, Debug)]
pub struct PathCheckpointCounter {
    pub only_fft: u64,
    pub only_dac: u64,
    pub both: u64,
    pub neither: u64,
}

//impl<'a> Sum<&'a PathCheckpointCounter> for PathCheckpointCounter {
//    fn sum<I: Iterator<Item = &'a PathCheckpointCounter>>(iter: I) -> Self {
//        iter.fold(Self::default(), |acc, x| Self {
//            only_fft: acc.only_fft + x.only_fft,
//            only_dac: acc.only_dac + x.only_dac,
//            both: acc.both + x.both,
//            neither: acc.neither + x.neither,
//        })
//    }
//}

impl Sum<PathCheckpointCounter> for PathCheckpointCounter {
    fn sum<I: Iterator<Item = PathCheckpointCounter>>(iter: I) -> Self {
        iter.fold(Self::default(), |acc, x| Self {
            only_fft: acc.only_fft + x.only_fft,
            only_dac: acc.only_dac + x.only_dac,
            both: acc.both + x.both,
            neither: acc.neither + x.neither,
        })
    }
}
