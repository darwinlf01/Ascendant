import os
import struct
import zlib

WIDTH = 64
HEIGHT = 32
ROOT = os.path.join(
    os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
    "src",
    "main",
    "resources",
    "assets",
    "ascendant",
    "textures",
    "models",
    "armor",
)


def write_png(path, pixels):
    def chunk(tag, data):
        return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

    raw = b"".join(b"\x00" + pixels[y * WIDTH * 4 : (y + 1) * WIDTH * 4] for y in range(HEIGHT))
    png = (
        b"\x89PNG\r\n\x1a\n"
        + chunk(b"IHDR", struct.pack(">IIBBBBB", WIDTH, HEIGHT, 8, 6, 0, 0, 0))
        + chunk(b"IDAT", zlib.compress(raw, 9))
        + chunk(b"IEND", b"")
    )
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as file:
        file.write(png)


def blank():
    return bytearray(WIDTH * HEIGHT * 4)


def set_px(buf, x, y, rgba):
    if 0 <= x < WIDTH and 0 <= y < HEIGHT:
        index = (y * WIDTH + x) * 4
        buf[index : index + 4] = bytes(rgba)


def rect(buf, x, y, w, h, fill, border):
    for yy in range(h):
        for xx in range(w):
            edge = xx < 1 or yy < 1 or xx >= w - 1 or yy >= h - 1
            set_px(buf, x + xx, y + yy, border if edge else fill)


def cuboid(buf, u, v, w, h, d, fill, border):
    rect(buf, u + d, v, w, d, fill, border)
    rect(buf, u + d + w, v, w, d, fill, border)
    rect(buf, u, v + d, d, h, fill, border)
    rect(buf, u + d, v + d, w, h, fill, border)
    rect(buf, u + d + w, v + d, d, h, fill, border)
    rect(buf, u + d + w + d, v + d, w, h, fill, border)


def chest(fill, border):
    buf = blank()
    cuboid(buf, 16, 16, 8, 12, 4, fill, border)
    cuboid(buf, 40, 16, 4, 12, 4, fill, border)
    return buf


PIECES = {
    "light_leather": ((166, 122, 74, 255), (110, 74, 40, 255)),
    "riveted_leather": ((92, 64, 48, 255), (150, 150, 150, 255)),
    "iron_mail": ((154, 160, 168, 255), (80, 86, 94, 255)),
    "caster_robe": ((58, 47, 110, 255), (196, 160, 72, 255)),
    "healer_robe": ((230, 220, 196, 255), (176, 148, 72, 255)),
}


def main():
    empty = blank()
    for name, (fill, border) in PIECES.items():
        write_png(os.path.join(ROOT, name + "_layer_1.png"), chest(fill, border))
        write_png(os.path.join(ROOT, name + "_layer_2.png"), empty)
    sample = open(os.path.join(ROOT, "light_leather_layer_1.png"), "rb").read(8)
    if sample != b"\x89PNG\r\n\x1a\n":
        raise SystemExit("png header mismatch")


if __name__ == "__main__":
    main()
