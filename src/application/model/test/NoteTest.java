package application.model.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import application.model.Note;
import application.model.exception.NoteInvalideException;

class NoteTest {

	@Test
	void testNoteValide() throws NoteInvalideException {
		Note noteValide = new Note(80, 100, "Très bien !");
		assertEquals(80, noteValide.getValeurNote());
		assertEquals(100, noteValide.getDenominateurNote());
		assertEquals("Très bien !", noteValide.getCommentaire());
	}

	@Test
	void testNoteSansCommentaire() throws NoteInvalideException {
		Note noteSansCommentaire = new Note(50, 75);
		assertEquals(50, noteSansCommentaire.getValeurNote());
		assertEquals(75, noteSansCommentaire.getDenominateurNote());
		assertEquals("", noteSansCommentaire.getCommentaire());
	}

	@Test
	void testNoteInvalideValeurNegative() {
		assertThrows(NoteInvalideException.class, () -> {
			new Note(-5, 20, "Commentaire invalide");
		});
	}

	@Test
	void testNoteInvalideValeurTropGrande() {
		assertThrows(NoteInvalideException.class, () -> {
			new Note(110, 100, "Commentaire invalide");
		});
	}

	@Test
	void testNoteInvalideDenominateurZero() {
		assertThrows(NoteInvalideException.class, () -> {
			new Note(30, 0, "Commentaire invalide");
		});
	}

	@Test
	void testNoteInvalideDenominateurTropGrand() {
		assertThrows(NoteInvalideException.class, () -> {
			new Note(25, 2000, "Commentaire invalide");
		});
	}

	@Test
	void testNoteInvalideDenominateurEgal1001() {
		assertThrows(NoteInvalideException.class, () -> {
			new Note(25, 1001, "Commentaire invalide");
		});
	}
	@Test
        void testNoteInvalideDenominateurEgal1000() {
                assertDoesNotThrow(()-> new Note(25, 1000, "Commentaire invalide"));
        }
}