package at.hannibal2.skyhanni.features.bingo.nextstep

class ChatMessageStep(displayName: String): NextStep(displayName)
class ObtainCrystalStep(val crystalName: String) : NextStep("Obtain a $crystalName Crystal")
