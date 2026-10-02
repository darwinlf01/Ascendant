import struct
import zlib
from pathlib import Path

SCALE = 4
WIDTH = 64 * SCALE
HEIGHT = 64 * SCALE
OUT = Path("src/main/resources/assets/ascendant/textures/entity/leather_quiver.png")

LEATHER = (104, 70, 44, 255)
LEATHER_DARK = (62, 40, 26, 255)
RIM = (78, 50, 32, 255)
WOOD = (118, 86, 52, 255)
WOOD_DARK = (78, 54, 32, 255)
FEATHER = (214, 204, 178, 255)
FEATHER_DARK = (150, 124, 88, 255)

pixels = bytearray(WIDTH * HEIGHT * 4)


def hash2(x, y):
    n = (int(x) * 374761393 + int(y) * 668265263) & 0xFFFFFFFF
    n = ((n ^ (n >> 13)) * 1274126177) & 0xFFFFFFFF
    return (n ^ (n >> 16)) & 255


def clamp(value):
    return max(0, min(255, int(value)))


def tint(color, delta):
    return (clamp(color[0] + delta), clamp(color[1] + delta), clamp(color[2] + delta), color[3])


def grain(x, y, amount):
    return (hash2(x, y) - 128) * amount // 128


def put(x, y, color):
    if 0 <= x < WIDTH and 0 <= y < HEIGHT:
        i = (y * WIDTH + x) * 4
        pixels[i : i + 4] = bytes(color)


def paint(x, y, w, h, color_at):
    x, y, w, h = int(x), int(y), int(w), int(h)
    if w <= 0 or h <= 0:
        return
    for py in range(y, y + h):
        for px in range(x, x + w):
            put(px, py, color_at(px, py, px - x, py - y, w, h))


def surface(base, dark, amount):
    def color(px, py, lx, ly, w, h):
        shaded = dark if lx == 0 or ly == 0 or lx == w - 1 else base
        return tint(shaded, grain(px, py, amount))

    return color


def paint_box(u, v, w, h, d, face):
    s = SCALE
    u, v, w, h, d = (u * s, v * s, w * s, h * s, d * s)
    paint(u + d, v, w, d, face)
    paint(u + d + w, v, w, d, face)
    paint(u, v + d, d, h, face)
    paint(u + d, v + d, w, h, face)
    paint(u + d + w, v + d, d, h, face)
    paint(u + d + w + d, v + d, w, h, face)


paint_box(0, 0, 2, 8, 2, surface(LEATHER, LEATHER_DARK, 16))
paint_box(10, 0, 3, 1, 3, surface(RIM, LEATHER_DARK, 10))
paint_box(0, 12, 1, 6, 1, surface(LEATHER_DARK, LEATHER_DARK, 10))
paint_box(8, 12, 1, 4, 1, surface(WOOD, WOOD_DARK, 8))
paint_box(14, 12, 1, 2, 1, surface(FEATHER, FEATHER_DARK, 10))

raw = b"".join(b"\x00" + bytes(pixels[y * WIDTH * 4 : (y + 1) * WIDTH * 4]) for y in range(HEIGHT))


def chunk(tag, data):
    return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)


png = (
    b"\x89PNG\r\n\x1a\n"
    + chunk(b"IHDR", struct.pack(">IIBBBBB", WIDTH, HEIGHT, 8, 6, 0, 0, 0))
    + chunk(b"IDAT", zlib.compress(raw, 9))
    + chunk(b"IEND", b"")
)
OUT.parent.mkdir(parents=True, exist_ok=True)
OUT.write_bytes(png)
print(OUT, len(png))
