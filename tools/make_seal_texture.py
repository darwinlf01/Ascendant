import struct
import zlib
from pathlib import Path

SCALE = 4
WIDTH = 64 * SCALE
HEIGHT = 64 * SCALE
OUT = Path("src/main/resources/assets/ascendant/textures/entity/holy_seal.png")

CORD = (148, 112, 58, 255)
CORD_DARK = (96, 70, 34, 255)
GOLD = (196, 154, 72, 255)
GOLD_DARK = (122, 88, 40, 255)
FACE = (214, 176, 88, 255)
RAY = (98, 64, 28, 255)

pixels = bytearray(WIDTH * HEIGHT * 4)


def hash2(x, y):
    n = (int(x) * 374761393 + int(y) * 668265263) & 0xFFFFFFFF
    n = ((n ^ (n >> 13)) * 1274126177) & 0xFFFFFFFF
    return (n ^ (n >> 16)) & 255


def clamp(value):
    return max(0, min(255, int(value)))


def tint(color, delta):
    return (clamp(color[0] + delta), clamp(color[1] + delta), clamp(color[2] + delta), color[3])


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


def metal(base, dark):
    def color(px, py, lx, ly, w, h):
        edge = lx == 0 or ly == 0 or lx == w - 1 or ly == h - 1
        return tint(dark if edge else base, (hash2(px, py) - 128) // 10)

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


def sun(px, py, lx, ly, w, h):
    dx = lx - (w - 1) / 2
    dy = ly - (h - 1) / 2
    distance = abs(dx) + abs(dy)
    if distance <= 1.2:
        return FACE
    if abs(dx) <= 0.6 or abs(dy) <= 0.6 or abs(abs(dx) - abs(dy)) < 0.6:
        return RAY
    return GOLD_DARK


paint_box(0, 0, 5, 1, 1, metal(CORD, CORD_DARK))
paint_box(0, 4, 1, 1, 4, metal(CORD, CORD_DARK))
paint_box(12, 0, 1, 3, 1, metal(CORD, CORD_DARK))
paint_box(0, 12, 3, 3, 1, metal(GOLD, GOLD_DARK))
paint_box(12, 6, 2, 2, 1, metal(FACE, GOLD_DARK))
paint(12 * SCALE + SCALE, 6 * SCALE + SCALE, 2 * SCALE, 2 * SCALE, sun)

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
