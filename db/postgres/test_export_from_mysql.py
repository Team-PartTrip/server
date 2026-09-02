import unittest

from db.postgres.export_from_mysql import literal


class LiteralTest(unittest.TestCase):

    def test_decodes_mysql_batch_escapes_once(self):
        self.assertEqual("'a\tb\nc\\d'", literal(r"a\tb\nc\\d"))

    def test_preserves_literal_backslash_t_and_n(self):
        self.assertEqual(r"'a\tb\nc'", literal(r"a\\tb\\nc"))

    def test_preserves_unknown_and_trailing_backslashes(self):
        self.assertEqual("'a\\xb\\'", literal("a\\xb\\"))


if __name__ == "__main__":
    unittest.main()
