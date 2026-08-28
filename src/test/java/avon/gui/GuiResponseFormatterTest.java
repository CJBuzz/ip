package avon.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GuiResponseFormatterTest {
    @Test
    void formatAvonResponse_consoleDecorations_removesOnlyPresentationMarkers() {
        String consoleResponse = """
                Avon:\tHere are the tasks in thy list:
                        1.[T][ ] rehearse Hamlet
                        2.[D][X] return script
                Avon:\tNow thou hast 2 tasks in thy list.""";

        String formattedResponse = GuiResponseFormatter.formatAvonResponse(consoleResponse);

        assertEquals("""
                Here are the tasks in thy list:
                1.[T][ ] rehearse Hamlet
                2.[D][X] return script
                Now thou hast 2 tasks in thy list.""", formattedResponse);
    }

    @Test
    void formatAvonResponse_contentContainingPrefix_preservesEmbeddedText() {
        String response = "Speak to Avon:\twhen rehearsal begins.";

        assertEquals(response, GuiResponseFormatter.formatAvonResponse(response));
    }
}
